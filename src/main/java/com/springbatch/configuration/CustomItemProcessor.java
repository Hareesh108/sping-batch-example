package com.springbatch.configuration;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.springbatch.model.Product;

@Component
public class CustomItemProcessor implements ItemProcessor<Product, Product> {

	
	
	
	
	
	@Override
	public Product process(Product item) throws Exception {
		
		try {
			int price = Integer.parseInt(item.getPrice()); 
			price *= 2;
	        item.setPrice(String.valueOf(price));
		} catch (Exception e) {
	         System.out.println(e);
		}
		return item;
		
	}
	
}


//try {
////  put the percentage logic
//  System.out.println(item.getDescription());
//  int discountPer = Integer.parseInt(item.getDiscount().trim());
//  double originalPrice = Double.parseDouble(item.getPrice().trim());
//  double discount = (discountPer / 100) * originalPrice;
//  double finalPrice = originalPrice - discount;
//  item.setDiscountedPrice(String.valueOf(finalPrice));
//} catch (
//      NumberFormatException ex
//) {
//  ex.printStackTrace();
//}
