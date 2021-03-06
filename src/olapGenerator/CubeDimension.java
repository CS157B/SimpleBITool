package olapGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CubeDimension {

	private String tableName;
	private String keyName;
	private boolean isGrouped;
	private Map<Integer, String> conceptHierarchy;
	private int currentHierarchyLevel;

	public CubeDimension(String tableName, String columnName, String keyName) {
		this(tableName, columnName, keyName, true);
	}
	
	public CubeDimension(String tableName, List<String> concepts, String keyName) {
		this(tableName, concepts, keyName, true);
	}

	public CubeDimension(String tableName, String initialColumnName, String keyName, boolean isGrouped) {
		this.tableName = tableName;
		this.keyName = keyName;
		this.isGrouped = isGrouped;
		this.conceptHierarchy = new TreeMap<>();
		currentHierarchyLevel = 0;
		this.conceptHierarchy.put(currentHierarchyLevel, initialColumnName);
		//conceptHierarchy.get(currentHierarchyLevel);
	}
	
	public CubeDimension(String tableName, List<String> concepts, String keyName, boolean isGrouped) {
		this.tableName = tableName;
		this.keyName = keyName;
		this.isGrouped = isGrouped;
		this.conceptHierarchy = new TreeMap<>();
		currentHierarchyLevel = 0;
		if(!concepts.isEmpty()){
			this.conceptHierarchy.put(currentHierarchyLevel, concepts.get(0));
			for(int i = 1; i < concepts.size(); i++){
				this.addConceptLevel(i, concepts.get(i));
			}
		}
		//conceptHierarchy.get(currentHierarchyLevel);
	}

	public Map<Integer, String> getConceptHierarchy() {
		return this.conceptHierarchy;
	}
	
	public String getCurrentConcept(){
		return conceptHierarchy.get(currentHierarchyLevel);
	}
	
	public List<String> getConceptList(){
		List<String> conceptList = new ArrayList();
		List<Integer> sortedKeys = conceptHierarchy.keySet().stream().sorted(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				if (o1 < o2) {
					return 1;
				} else if (o1 > o2) {
					return -1;
				} else {
					return 0;
				}
			}

		}).collect(Collectors.toList());
		
		for(int i : sortedKeys){
			conceptList.add(conceptHierarchy.get(i));
		}
		return conceptList;
	}

	public boolean addConceptLevel(int hierarchyLevel, String columnName) {
		if (conceptHierarchy.containsKey(hierarchyLevel)) {
			return false;
		}
		conceptHierarchy.put(hierarchyLevel, columnName);
		return true;
	}
	
	public boolean removeConceptLevel(int conceptLevel){
		if(conceptHierarchy.containsKey(conceptLevel)){
			conceptHierarchy.remove(conceptLevel);
			return true;
		}else{
			return false;
		}
	}
	
	public boolean removeConcept(String concept){
		for(int i : conceptHierarchy.keySet()){
			if(conceptHierarchy.get(i).equals(concept)){
				conceptHierarchy.remove(i);
				return true;
			}
		}
		return false;
	}

	public boolean rollUp() {
		System.out.print(conceptHierarchy.get(currentHierarchyLevel) + " > ");
		List<Integer> sortedKeys = conceptHierarchy.keySet().stream().sorted(new Comparator<Integer>() {
			
			

			@Override
			public int compare(Integer o1, Integer o2) {
				if (o1 < o2) {
					return -1;
				} else if (o1 > o2) {
					return 1;
				} else {
					return 0;
				}
			}

		}).collect(Collectors.toList());

		Iterator<Integer> itr = sortedKeys.iterator();
		while (itr.hasNext()) {
			int i = itr.next();
			if (i > currentHierarchyLevel) {
				currentHierarchyLevel = i;
				System.out.println(conceptHierarchy.get(currentHierarchyLevel));
				return true;
			}
		}
		return false;
	}

	public boolean drillDown() {
		String temp = conceptHierarchy.get(currentHierarchyLevel);
		
		List<Integer> reverseSortedKeys = conceptHierarchy.keySet().stream().collect(Collectors.toList());

		reverseSortedKeys.sort(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				if (o1 < o2) {
					return 1;
				} else if (o1 > o2) {
					return -1;
				} else {
					return 0;
				}
			}

		});

		Iterator<Integer> itr = reverseSortedKeys.iterator();
		while (itr.hasNext()) {
			int i = itr.next();
			if (i < currentHierarchyLevel) {
				currentHierarchyLevel = i;
				System.out.print(conceptHierarchy.get(currentHierarchyLevel) + " < " + temp);
				return true;
			}
		}
		return false;
	}

	public boolean hasConcept(String columnName){
		for(Integer i : conceptHierarchy.keySet()){
			if(conceptHierarchy.get(i) == columnName){
				return true;
			}
		}
		return false;
	}

	public String getColumnName() {
		return conceptHierarchy.get(currentHierarchyLevel);
	}

	public String getTableName() {
		return this.tableName;
	}

	public String getKeyName() {
		return this.keyName;
	}

	public boolean isGrouped() {
		return this.isGrouped;
	}

	public void setGrouped(boolean isGrouped) {
		this.isGrouped = isGrouped;
	}
}
