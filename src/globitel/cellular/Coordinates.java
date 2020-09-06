package globitel.cellular;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class Coordinates {

	private String name;
	private String coordinates;
	private List<Document> cells = new ArrayList<Document>();
	
	final List<List<Double>> polygons = new ArrayList<>();
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	public void search(String coor) {

		String[] coors = coor.split("a");
		try (MongoClient mongoClient = MongoClients.create()) {

			MongoDatabase database = mongoClient.getDatabase("cellular");
			MongoCollection<Document> collection = database.getCollection("cells");
			
			for (int i = 0; i < coors.length; i++) {
				String[] coo = coors[i].split(",");
				System.out.println("coors" + i + " : " + coors[i]);
				polygons.add(Arrays.asList(Double.parseDouble(coo[0]), Double.parseDouble(coo[1])));
			}
			Bson query = Filters.geoWithinPolygon("location", polygons);
			
			FindIterable<Document> results = collection.find(query).projection(new Document("_id", 1));
			MongoCursor<Document> resultsIterator = results.iterator();
			
			try {
				while (resultsIterator.hasNext()) {
					cells.add(resultsIterator.next());
					
				}
			} finally {
				resultsIterator.close();
				System.out.println(cells);
			}
		
			
		}

	}
	
	public void insertArea() {
		
		try (MongoClient mongoClient = MongoClients.create()) {

			MongoDatabase database = mongoClient.getDatabase("cellular");
			MongoCollection<Document> collection = database.getCollection("areas");
			collection.insertOne(new Document("name", name).append("date", new Date()).append("type", "public").append("IsDeleted", false).append("location", new Document("type", "Point")
								.append("coordinates",polygons)).append("cells", cells));
		
		}

	}

}
