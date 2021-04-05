package eci.ieti;

import eci.ieti.data.CustomerRepository;
import eci.ieti.data.ProductRepository;
import eci.ieti.data.TodoRepository;
import eci.ieti.data.UserRepository;
import eci.ieti.data.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfiguration.class);
        MongoOperations mongoOperation = (MongoOperations) applicationContext.getBean("mongoTemplate");

        userRepository.deleteAll();

        for(int i=0;i<10;i++){
            userRepository.save(new User("user"+i, "user"+i+"@mail.com"));
        }

        userRepository.findAll().stream().forEach(System.out::println);

        System.out.println();

        todoRepository.deleteAll();

        for(int i=0;i<25;i++){
            Integer date = (i%21)+10;
            todoRepository.save(new Todo("todo",i,"2021-04-"+date,"user"+i%10+"@mail.com","hola"));
        }
        todoRepository.save(new Todo("todo",1,"2021-04-03","user1@mail.com","hola"));

        System.out.println();

        LocalDate date = LocalDate.now();
        Query query = new Query();
        query.addCriteria(Criteria.where("dueDate").lt(date.toString()));

        List<Todo> todos = mongoOperation.find(query,Todo.class);
        List<Todo> todos2=todoRepository.findByDueDateBefore(date.toString());
        System.out.println(todos.size());
        System.out.println(todos2.size());

        Query query1= new Query();
        query1.addCriteria(Criteria.where("responsible").is("user5@mail.com")
                .and("priority").gte(5));

        List<Todo> todosResponsible2=todoRepository.findByResponsibleEqualsAndPriorityIsGreaterThanEqual("user5@mail.com",5);

        List<Todo> todosResponsible = mongoOperation.find(query1,Todo.class);
        System.out.println(todosResponsible.size());
        System.out.println(todosResponsible2.size());

        todoRepository.save(new Todo("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",1,"2021-04-05","user1@mail.com","hola"));
        Query query2 = new Query();
        query2.addCriteria(Criteria.where("description").regex("^.{30,}$"));
        List<Todo> todosDescription = mongoOperation.find(query2,Todo.class);
        List<Todo> todosDescription2 = todoRepository.findByDescriptionMatchesRegex("^.{30,}$");
        System.out.println(todosDescription.size());
        System.out.println(todosDescription2.size());





    }

}