package com.example.oauth.item;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemRestController {

	@GetMapping("/list")
	public List<Item> getList(){
		List<Item> list = new ArrayList<>();
		list.add(new Item("00001", "ノートPC"));
		list.add(new Item("00002", "携帯電話"));
		list.add(new Item("00003", "バナナ"));
		return list;
	}
}
