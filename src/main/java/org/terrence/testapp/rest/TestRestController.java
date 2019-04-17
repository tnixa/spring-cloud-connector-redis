package org.terrence.testapp.rest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.terrence.testapp.model.Person;

@RestController
public class TestRestController {

  @Autowired
  private CrudRepository<Person,String> repo;

  @RequestMapping(value="/test", produces="text/plain")
  public String runTest() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    
    try {
      pw.println("Beginning test.");
      Person test = new Person("TestPerson", 33);
      String id = UUID.randomUUID().toString(); // use a random id
      pw.println("Using random Person id: " + id);
      test.setId(id);

      // verify there is nothing in the repo with the id and then create the test object
      try {
        pw.println("Testing for existing id '"+id+"' and  deleting as required.");
        // if there is an existing Person with the id then delete it.
        repo.findById(id).ifPresent(p -> repo.deleteById(p.getId()));
        // attempt save of new Person.
        try{
          pw.println("Saving new person with id '"+id+"'");
          repo.save(test);
          // still here? save says it worked.. lets see...
          try{
            // retrieve the Person by ID and make sure the name and id matche
            Person check = repo.findById(id).get();
            if (((check.getId() == null && test.getId() == null)
                || (check.getId() != null && check.getId().equals(test.getId())))
                && ((check.getName() == null && test.getName() == null)
                    || (check.getName() != null && check.getName().equals(test.getName())))) {
              pw.println("PASS: retrieved object matched saved object");
            } else {
              pw.println("FAIL: retrieved object did NOT match saved object");
            }
          }catch(Exception e) {
            pw.println("FAIL: Problem during retrieve of saved person... ");
            e.printStackTrace(pw);
          } 
        }catch(Exception e) {
          pw.println("FAIL: Problem saving new person... ");
          e.printStackTrace(pw);
        }
      } catch (Exception e) {
          pw.println("FAIL: Problem during find/delete for existing. ");
          e.printStackTrace(pw);
      }
    } catch (Exception e) {
      pw.println("FAIL: Unexpected error during test.");
      e.printStackTrace(pw);
    }
    pw.flush();
    return sw.toString();
  }
}