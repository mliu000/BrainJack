package muyel.controller;

import muyel.model.*;
import muyel.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blackjack")
@CrossOrigin(origins = "http://localhost:3000")
public class GameController {

    @Autowired
    private GameService gameService;

    ///// METHODS TO CONNECT TO FRONTEND /////




    

    // 
}