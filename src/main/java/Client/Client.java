package Client;
import com.example.rest1.Name;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;


public class Client {
    private String baseURL = "http://localHost:8080";
    private Scanner input;
    private RestTemplate restTemplate = new RestTemplate();


    public static void main(String[] args)
    {
        Client cl = new Client();
        cl.input = new Scanner(System.in);
        boolean again = true;
        do
        {
            int choice = cl.getChoice();
            switch(choice)
            {
                case 1:
                {
                    cl.getName();
                    break;
                }
                case 2:
                {
                    cl.createName();
                    break;
                }
                case 3:
                {
                    cl.updateName();
                    break;
                }
                case 4:
                {
                    cl.deleteName();
                    break;
                }
                case 5:
                    cl.getAllNames();
                    break;
                default:
                {
                    again = false;
                }
            }

        }while(again);


        cl.input.close();
    }

    //Retieve a name by ID
    public void getName() {
        try {
            int id = getID("retrieve");
            String url = baseURL + "/names/" + id;

            Name name = restTemplate.getForObject(url, Name.class);
            System.out.println(name.toString());
        }
        catch(HttpClientErrorException hce) {
            System.out.println(parseMessage(hce.getMessage()));
        }
    }

    //Create a Name object
    public void createName()
    {
        String url = baseURL + "/names";
        try {
            System.out.println("What is the name that you would like to create?");
            Name name = restTemplate.postForObject(url, new Name(1,input.nextLine()), Name.class);
            System.out.println(name.toString());
        }
        catch(HttpClientErrorException hce) {
            System.out.println(parseMessage(hce.getMessage()));
        }
    }

    //Update a selected name
    public void updateName()
    {
        try {
            Name nameToSend = new Name();
            int id = getID("update");
            nameToSend.setId(id);
            System.out.println("What is the new name?");
            nameToSend.setName(input.nextLine());
            String url = baseURL + "/names/";
            restTemplate.put(url, nameToSend, Name.class);
            url += id;
            Name name = restTemplate.getForObject(url, Name.class);
            System.out.println(name.toString());
        }
        catch(HttpClientErrorException hce) {
            System.out.println(parseMessage(hce.getMessage()));
        }

    }

    //Delete a selected name
    public void deleteName()
    {
        try
        {
            int id = getID("delete");
            String url = baseURL + "/names/{id}";
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", id + "");
            restTemplate.delete(url,params);
        }
        catch(HttpClientErrorException hce) {
            System.out.println(parseMessage(hce.getMessage()));
        }
    }

    //Get all names
    public void getAllNames()
    {
        try {
            String url = baseURL + "/getNames";
            HttpHeaders headers = new HttpHeaders();
            Name name = new Name();
            HttpEntity<Name> entity = new HttpEntity<>(name,headers);
            ResponseEntity<List<Name>> result = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Name>>() {
            });
            List<Name> list = result.getBody();
            for(Name names: list){
                System.out.println("Name id=" + names.getId() + ", name= " + names.getName());
            }
            //System.out.println(list.toString());
        }
        catch(HttpClientErrorException hce) {
            System.out.println(parseMessage(hce.getMessage()));
        }

    }

    private int getChoice()
    {

        int choice = 0;
        do
        {
            try
            {
                System.out.println("1.  Retrieve a name by ID");
                System.out.println("2.  Create a Name object");
                System.out.println("3.  Update a selected name");
                System.out.println("4.  Delete a selected name");
                System.out.println("5. Get all names");
                System.out.println("6. Exit");
                System.out.println("\nPlease enter your choice.");
                choice = input.nextInt();
            }
            catch (InputMismatchException ime)
            {
                System.out.println("You must enter an integer between 1 and 6");
            }
            finally
            {
                input.nextLine();
            }
        } while (choice < 1 || choice > 6);
        return choice;
    }

    private int getID(String action)
    {
        boolean valid = false;
        int id = 0;
        do
        {
            try
            {
                System.out.println("Please enter the ID of the Name to " + action);
                id = input.nextInt();
                valid = true;
            }
            catch(InputMismatchException ime)
            {
                System.out.println("You must enter an integer for the id");
            }
            finally
            {
                input.nextLine();
            }
        }while (!valid);
        return id;
    }

    private String parseMessage(String message) {
        int start = message.lastIndexOf(":", message.lastIndexOf(",") -1 ) +2;
        int end = message.lastIndexOf(",") -1;
        message = message.substring(0,6) + message.substring(start, end);
        return message;
    }
}
