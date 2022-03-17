package product;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped

public class ItemController {

	private List<Item> items;
	private List<Integer> itemsInSal;
	private List<String> itemsNamesInSal;
	private ItemDbUtil itemDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());
	int money=0;
	
	public ItemController() throws Exception {
		items = new ArrayList<>();
		itemsInSal = new ArrayList<>();
		itemsNamesInSal = new ArrayList<>();
		itemDbUtil = ItemDbUtil.getInstance();
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public String getItemsInSal() {
		return itemsInSal.toString();
	}
	public String getItemsNamesInSal() {
		return itemsNamesInSal.toString();
	}
	public void loadItems() {

		logger.info("Loading items");
		
		items.clear();

		try {
			
			// get all items from database
			items = itemDbUtil.getItems();
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading items", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
	}
		
	public String addItem(Item theItem) {

		logger.info("Adding item: " + theItem);

		try {
			
			// add item to the database
			itemDbUtil.addItem(theItem);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error adding items", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);

			return null;
		}
		
		return "adminPage";
	}

	public String loadItem(int itemId) {
		
		logger.info("loading item: " + itemId);
		
		try {
			// get item from database
			Item theItem = itemDbUtil.getItem(itemId);
			
			// put in the request attribute ... so we can use it on the form page
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();		

			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("item", theItem);	
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading item id:" + itemId, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
				
		return "update-item-form.xhtml";
	}	
	
	public String updateItem(Item theItem) {

		logger.info("updating item: " + theItem);
		
		try {
			
			// update item in the database
			itemDbUtil.updateItem(theItem);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error updating item: " + theItem, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
		
		return "adminPage?faces-redirect=true";		
	}
	
	public String deleteItem(int itemId) {

		logger.info("Deleting item id: " + itemId);
		
		try {

			// delete the item from the database
			itemDbUtil.deleteItem(itemId);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error deleting item id: " + itemId, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
		
		return "adminPage";	
	}	
	public void addToSal(int toSal, int money1, String name, int amount1) {
		if(amount1>0) {
		itemsInSal.add(toSal);
		itemsNamesInSal.add(name);
		money= money+money1;
		System.out.println("add to sal id" + toSal );
		}
		else
			System.out.println("Item's amount is 0");
			
	}
	public void deleteFromBasket(int toSal, int money1, String name) {
		System.out.println("delete from sal id:" + toSal);
		int index= itemsInSal.indexOf(toSal);
		System.out.println("index id:" + index);
		itemsInSal.remove(index);
		itemsNamesInSal.remove(index);
		money= money-money1;
	}
	
	public int getPurchase() {
		return money;
	}
	
	public String reduceAmounts() throws Exception {
		 for (int id : itemsInSal) {  
	         System.out.println("update item amount: " + id); 
	     	 Item theItem = itemDbUtil.getItem(id);
	     	Item updatedItem=new Item(id, theItem.getName(), theItem.getPrice(), theItem.getAmount()-1, theItem.getLink());
	     	updateItem(updatedItem);
	     	
	      } 
		 itemsInSal.clear();
	    itemsNamesInSal.clear();
	    money=0;
	    
		return "home";
	}
	
	
	
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
}
