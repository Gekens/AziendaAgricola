package com.example.giacomo.aziendaagricola;

/**
 * Created by Giacomo on 28/06/2016.
 */
public class Prodotti {
    String qualità;
    String disponibilità;
    String image;

    public Prodotti( String image, String qualità, String disponibilità) {
        this.image = image;
        this.qualità = qualità;
        this.disponibilità = disponibilità;
    }

    public String getQualità() {
        return qualità;
    }

    public void setQualità(String qualità) {
        this.qualità = qualità;
    }

    public String getDisponibilità() {
        return disponibilità;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDisponibilità(String disponibilità) {
        this.disponibilità = disponibilità;
    }
}
