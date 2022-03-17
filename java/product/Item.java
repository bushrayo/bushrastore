package product;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class Item {

	private int id;
	private String name;
	private int price;
	private int amount;
	private String link;
	
	public Item() {
	}
	
	public Item(int id2, String name2, int price2, int amount2, String link2) {
		this.id=id2;
		this.name=name2;
		this.price=price2;
		this.amount=amount2;
		this.link=link2;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", price="
				+ price + ", amount=" + amount + ", link=" + link + "]";
	}

}
