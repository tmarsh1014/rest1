package com.example.rest1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Rest1Controller {
    private int id = 0;
    private ArrayList<Name> names;

    public Rest1Controller() {
        names = new ArrayList<>();
        names.add(new Name(++id,"Taylor"));
        names.add(new Name(++id,"Sedina"));
        names.add(new Name(++id,"Henry"));
        names.add(new Name(++id,"Greatzel"));
        names.add(new Name(++id,"Matt"));
    }

    @RequestMapping(value = "names/{id}",method = RequestMethod.GET)
    public Name getName(@PathVariable int id){
        int index = findName(id);
        return names.get(index);
    }

    @RequestMapping(value="names",method= RequestMethod.POST)
    public Name createName(@RequestBody Name name){
        name.setId(++id);
        names.add(name);
        return name;
    }

    @RequestMapping(value = "names",method = RequestMethod.PUT)
    public Name updateName(@RequestBody Name name){
        int index = findName(name.getId());
        names.get(index).setName(name.getName());
        return names.get(index);
    }

    @RequestMapping(value = "names/{id}",method = RequestMethod.DELETE)
    public String deleteName(@PathVariable int id){
        int index = findName(id);
        names.remove(index);
        return id + " deleted.";
    }

    @RequestMapping(value = "/getNames",method = RequestMethod.GET)
    public List<Name> getNames(){
        return names;
    }



    @RequestMapping(value="namesList",method= RequestMethod.POST)
    public List<Name> namesList(@RequestBody List<Name> names){
        for(Name name: names){
            name.setId(++id);
            this.names.add(name);
        }
        return this.names;
    }

    private int findName(int id){
        int found = -1;
        for (int i = 0; i < names.size(); i++) {
            if(names.get(i).getId() == id){
                found = i;
            }
        }
        if(found < 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID" + found + " not found");
        }
        return found;
    }

}
