package org.example;

import entities.utilisateur;
import service.utilisateurService;
import service.DonsService;
import service.DemandeDonsService;
import entities.DemandeDons;

import java.sql.Timestamp;

public class Main {
    public static void main(String[] args) {
        // Création d'une instance de DemandeDonsService
        // Créer un utilisateur pour tester
        utilisateur user = new utilisateur();
        user.setIdUser(1); // Remplacez 1 par l'ID de l'utilisateur que vous souhaitez utiliser
        user.setNbPoints(100); // Supposons que l'utilisateur a 100 points

        // Créer une instance de DonsService
        DonsService donsService = new DonsService();

        // Appeler la méthode addDonsForDemande pour ajouter un don pour une demande spécifique
        int donPoints = 40; // Supposons que l'utilisateur fait un don de 50 points
        int idDemande = 1; // Remplacez 1 par l'ID de la demande à laquelle vous souhaitez ajouter un don
        donsService.addDonsForDemande(user, donPoints, idDemande);
    }

    }
