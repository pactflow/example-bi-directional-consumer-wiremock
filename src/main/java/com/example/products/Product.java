package com.example.products;

class Product {
	private String id;
	private String name;
	private String type;
	private Double price;

	Product() {
	}

	Product(final String id, final String name, final String type, final Double price) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.price = price;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public Double getPrice() {
		return this.price;
	}

	public String getType() {
		return this.type;
	}

}