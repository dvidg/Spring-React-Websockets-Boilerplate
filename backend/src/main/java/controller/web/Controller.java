/***********************************************
 *            Controller.java                  *
 *                                             *
 *   Handles requests from the client.         *
 *   It takes requests at web addresses        *
 *   specified in GetMapping annotation        *
 *   gets data from the model to return        *
 *                                             *
 * ********************************************/
 
package controller;

import map.*;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

//annotation sets up CORS support, Controller and /api root 
@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/api")
public class Controller {

    public Map a = new Map();

    @RequestMapping("/map/click")
    @ResponseBody
    public String validClick(@RequestBody String value) {
        System.out.println(value);
	return (value);
    }
    
/*    //return visual map as 2d array
    @CrossOrigin
    @GetMapping("/visualMap")
    int[][] test() {
        return a.multiplyVisualMap();
    }
*/
}
