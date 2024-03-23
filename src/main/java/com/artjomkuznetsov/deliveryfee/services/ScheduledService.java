package com.artjomkuznetsov.deliveryfee.services;

import com.artjomkuznetsov.deliveryfee.models.WeatherData;
import com.artjomkuznetsov.deliveryfee.repositories.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@EnableScheduling
public class ScheduledService {
    private static final String ESTONIAN_WEATHER_API_URL = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
    private static final  List<String> STATIONS = List.of("Tallinn-Harku", "Tartu-Tõravere", "Pärnu");

    private final RestTemplate restTemplate;
    private final WeatherDataRepository weatherDataRepository;

    @Autowired
    public ScheduledService(RestTemplate restTemplate, WeatherDataRepository weatherDataRepository) {
        this.restTemplate = restTemplate;
        this.weatherDataRepository = weatherDataRepository;
    }

    /**
     * This method retrieves weather data from the <a href="https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php">Estonian weather API</a>,
     * parses XML responses, and saves relevant observations to the database.
     * It's scheduled to run periodically using the @Scheduled annotation with a specified cron expression.
     */
    @Scheduled(cron = "0 15 * * * *")
    public void getWeatherDataFromAPI() {
        String responseData = restTemplate.getForObject(ESTONIAN_WEATHER_API_URL, String.class);
        if (responseData != null) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                ByteArrayInputStream input = new ByteArrayInputStream(
                        responseData.getBytes(StandardCharsets.UTF_8));

                Document doc = builder.parse(input);
                NodeList nodeList = doc.getElementsByTagName("station");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String stationName = element.getElementsByTagName("name").item(0).getTextContent();

                        Long timestamp = Long.parseLong(doc.getElementsByTagName("observations").item(0)
                                .getAttributes().getNamedItem("timestamp").getTextContent());

                        if (STATIONS.contains(stationName)) {
                            WeatherData weatherData = new WeatherData();
                            weatherData.setStation(stationName);
                            weatherData.setStationWMO(element.getElementsByTagName("wmocode").item(0).getTextContent());
                            String airTemp = element.getElementsByTagName("airtemperature").item(0).getTextContent();
                            if (!airTemp.isEmpty()) {
                                weatherData.setAirTemperature(Float.parseFloat(airTemp));
                            }
                            String windSpeed = element.getElementsByTagName("windspeed").item(0).getTextContent();
                            if (!windSpeed.isEmpty()) {
                                weatherData.setWindSpeed(Float.parseFloat(windSpeed));
                            }
                            weatherData.setWeatherPhenomenon(element.getElementsByTagName("phenomenon").item(0).getTextContent());
                            weatherData.setObservationTimestamp(timestamp);
                            weatherDataRepository.save(weatherData);
                        }
                    }
                }
            } catch (SAXException e) {
                System.out.println("An error occurred while parsing the XML document with weather data. Please ensure that the XML document has the correct structure and try again.");
            } catch (IOException e) {
                System.out.println("An error occurred while processing weather data.");
            } catch (ParserConfigurationException e) {
                System.out.println("An error occurred while configuring the XML parser.");
            }
        }
    }
}
