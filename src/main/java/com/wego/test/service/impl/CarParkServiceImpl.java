package com.wego.test.service.impl;

import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.opencsv.CSVReader;
import com.wego.test.model.CarParkAvailability;
import com.wego.test.model.CarParkInformation;
import com.wego.test.repository.CarParkAvailabilityRepository;
import com.wego.test.repository.CarParkAvailabilityRepository.CarParkAvailabilityDTO;
import com.wego.test.repository.CarParkInformationRepository;
import com.wego.test.service.CarParkService;
import com.wego.test.util.ApiAppResponse;
import com.wego.test.util.AppUtil;

@Service
public class CarParkServiceImpl implements CarParkService {

	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PER_PAGE = 10;
	private static final int MAX_QUERY_PARAM = 2000;
	Logger LOGGER = LoggerFactory.getLogger(CarParkServiceImpl.class);
	@Autowired CarParkInformationRepository carParkInformationRepository;
	@Autowired CarParkAvailabilityRepository carParkAvailabilityRepository;
	
	@Override
	public String initDefaultData() {
//		createCardParkInformationWittLatLonCSV();//for generating information csv file purpose on local
		if (carParkInformationRepository.count()==0) {
			//only load card park information once 
			insertCardParkInformationToDB();
		}
		insertCardParkAvailabilityToDB();
		return "Load default data successfully.";
	}
	
	@Override
	public List<CarParkAvailabilityDTO> getNearestLocation(Double latitude, Double longitude, Integer page, Integer per_page) {
		if (page==null || page <0) page = DEFAULT_PAGE;
		if (per_page==null || per_page<=0) per_page = DEFAULT_PER_PAGE;
		return carParkAvailabilityRepository.getCarParkAvailabilities(longitude, latitude, per_page, page);
	}

	@SuppressWarnings({ "unchecked" })
	private void insertCardParkAvailabilityToDB(){
	    try { 
	    	String url = "https://api.data.gov.sg/v1/transport/carpark-availability";
			ApiAppResponse resp = AppUtil.callAPI(url, HttpMethod.GET, null, null);
			if (resp.success()) {
				
//			{
//	          "carpark_info": [
//	            {
//	              "total_lots": "105",
//	              "lot_type": "C",
//	              "lots_available": "29"
//	            }
//	          ],
//	          "carpark_number": "HE12",
//	          "update_datetime": "2021-07-15T17:08:35"
//	        },
				Map<String, Object> data = (Map<String, Object>) resp.getData();
				List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");
				List<CarParkAvailability> list = items.stream()
					.findFirst()
					.map(e -> (List<Map<String, Object>>)e.get("carpark_data"))
					.get()
					.stream()
					.map(this::buildCarParkAvailability)
					.filter(Objects::nonNull).collect(Collectors.toList());
				
				if (!list.isEmpty()) {
					//delete current data
					carParkAvailabilityRepository.deleteAll();
				}
				if (list.size()>MAX_QUERY_PARAM) {
					List<List<CarParkAvailability>> subList = ListUtils.partition(list, MAX_QUERY_PARAM);
					subList.stream().forEach(lst -> carParkAvailabilityRepository.saveAll(lst));
				} else {
					carParkAvailabilityRepository.saveAll(list);
				}
			}
	        
	    } catch (Exception e) { 
	    	LOGGER.error("Error when inserting car park infirmation: {}", e.getMessage());
	    }
	}
	
	@SuppressWarnings("unchecked")
	private CarParkAvailability buildCarParkAvailability(Map<String, Object> carpark) {
		CarParkAvailability cpa = null;
		try {
			List<Map<String, Object>> carpark_info = (List<Map<String, Object>>) carpark.get("carpark_info");
			cpa = carpark_info.stream().findFirst().map(e -> setCarParkAvailabilityValue(carpark, e)).get();
	    } catch (Exception e) {
	    	LOGGER.error("Error when building CarParkAvailability: {}", e.getMessage());
	    }
		return cpa;
	}
	
	private CarParkAvailability setCarParkAvailabilityValue(Map<String, Object> carpark, Map<String, Object> e) {
		CarParkAvailability cpa = (CarParkAvailability)convertJsonToObject(e, CarParkAvailability.class);
		cpa.setCar_park_no(MapUtils.getString(carpark, "carpark_number"));
		cpa.setUpdate_datetime(toDate(MapUtils.getString(carpark, "update_datetime")));
		return cpa;
	}
	
	public Object convertJsonToObject(Map<String, Object> map, Class<?> clazz) {
		try {
			Gson gson = new Gson();
			JsonElement jsonElement = gson.toJsonTree(map);
			return gson.fromJson(jsonElement, clazz);
		}catch (Exception e) {
			return null;
		}
	}
	
	public static Date toDate(String dateInString){
		String DATE_FORMAT_STR_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss";
//		 "update_datetime": "2021-07-15T17:08:35"
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_STR_ISO8601);
			return formatter.parse(dateInString);
		} catch (Exception e) {
			System.out.print(e);
		}	 
		return new Date();
	}
	private void insertCardParkInformationToDB(){
	    try { 
	        FileReader fileReader = new FileReader("src/main/resources/hdb-carpark-information-with-latlon.csv"); 
	        CSVReader csvReader = new CSVReader(fileReader); 
	        csvReader.readNext();// first line
	        String[] nextRecord; 
	        List<CarParkInformation> list = new ArrayList<>();
	        while ((nextRecord = csvReader.readNext()) != null) {
	        	CarParkInformation carParkInfo = buildCarParkInformation(nextRecord);
	        	if (carParkInfo!=null) {
	        		list.add(carParkInfo);
	        	}
	        }
	        csvReader.close();
	        fileReader.close();
	        
	        if (list.size()>MAX_QUERY_PARAM) {
	        	List<List<CarParkInformation>> subList = ListUtils.partition(list, MAX_QUERY_PARAM);
	        	subList.stream().forEach(lst -> carParkInformationRepository.saveAll(lst));
	        } else {
	        	carParkInformationRepository.saveAll(list);
	        }
	        
	    } catch (Exception e) { 
	    	LOGGER.error("Error when inserting car park infirmation: {}", e.getMessage());
	    }
	} 
	
	private CarParkInformation buildCarParkInformation(String[] record) {
		CarParkInformation carParkInfo = new CarParkInformation();
		try {
			carParkInfo.setCar_park_no(record[0]);
			carParkInfo.setAddress(record[1]);
			carParkInfo.setX_coord(Double.parseDouble(record[2]));
			carParkInfo.setY_coord(Double.parseDouble(record[3]));
			carParkInfo.setLatitude(Double.parseDouble(record[12]));
			carParkInfo.setLongitude(Double.parseDouble(record[13]));
			carParkInfo.setCar_park_type(record[4]);
			carParkInfo.setType_of_parking_system(record[5]);
			carParkInfo.setShort_term_parking(record[6]);
			carParkInfo.setFree_parking(record[7]);
			carParkInfo.setNight_parking(record[8]);
			carParkInfo.setCar_park_decks(Integer.parseInt(record[9]));
			carParkInfo.setGantry_height(Double.parseDouble(record[10]));
			carParkInfo.setCar_park_basement(record[11]);
			return carParkInfo;
		}catch (Exception e) {
			LOGGER.error("Error when building CarParkInformation {}", e.getMessage());
			return null;
		}
	}
	
	private void createCardParkInformationWittLatLonCSV(){
	    try { 
	        FileReader fileReader = new FileReader("src/main/resources/hdb-carpark-information.csv"); 
	        CSVReader csvReader = new CSVReader(fileReader); 
	        csvReader.readNext();// first line
	        String[] nextRecord; 
	        List<String> line = Arrays.asList("car_park_no", "address",	"x_coord", "y_coord", "car_park_type", "type_of_parking_system", "short_term_parking", "free_parking",
	        		"night_parking", "car_park_decks", "gantry_height",	"car_park_basement", "latitude", "longitude");
	        allCarParks.clear();
	        allCarParks.add(StringUtils.join(line, ","));
	        while ((nextRecord = csvReader.readNext()) != null) {
	        	getLatLong(nextRecord, nextRecord[2], nextRecord[3]);
	        }
	        csvReader.close();
	        fileReader.close();
	        
	        //write file
	        FileWriter writer = new FileWriter("c:\\working\\hdb-carpark-information-with-latlon.csv"); 
	        for(String str: allCarParks) {
	          writer.write(str + System.lineSeparator());
	        }
	        writer.close();
	    } catch (Exception e) { 
	    	LOGGER.error("Error when reading file: " + e.getMessage());
	    }
	    
	} 
	List<String> allCarParks = new ArrayList<String>();
	@SuppressWarnings("unchecked")
	private void getLatLong(String[] nextRecord, String x_coord, String y_coord) {
		try {
			List<String> line = new ArrayList<String>(Arrays.asList(nextRecord));
			String url = String.format("https://developers.onemap.sg/commonapi/convert/3414to4326?X=%s&Y=%s", x_coord, y_coord);
			ApiAppResponse resp = AppUtil.callAPI(url, HttpMethod.GET, null, null);
			if (resp.success()) {
//			{"latitude":1.3197295716669164,"longitude":103.84215843333567}
				Map<String, Object> result = (Map<String, Object>) resp.getData();
				line.add(String.valueOf(result.get("latitude")));
				line.add(String.valueOf(result.get("longitude")));
				
				allCarParks.add(String.join(",", line
						.stream()
						.map(name -> ("\"" + name + "\""))
						.collect(Collectors.toList())));
			}
		}  catch (Exception e) {
			LOGGER.error("Error: unable to get lat/lon");
		}
	}
	
}
