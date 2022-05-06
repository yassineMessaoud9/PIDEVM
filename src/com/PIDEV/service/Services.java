/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.PIDEV.service;

import com.PIDEV.entities.Plat;
import com.PIDEV.utils.ConnectAPii;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jdk.internal.dynalink.beans.StaticClass;

/**
 *
 * @author Asus
 */
public class Services {

    public static Services instance = null;

    private ConnectionRequest req;
    public ArrayList<Plat> Plat;

    public static Services getInstance() {
        if (instance == null) {
            instance = new Services();
        }
        return instance;
    }

    public Services() {
        req = new ConnectionRequest();
    }

    public ArrayList<Plat> parsePlat(String jsonText) {
        try {

            System.out.println(jsonText);
            Plat = new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String, Object> tasksListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String, Object>> list = (List<Map<String, Object>>) tasksListJson.get("root");

            for (Map<String, Object> obj : list) {
                Plat a = new Plat();

                float id = Float.parseFloat(obj.get("idplat").toString());
                a.setIdPlat((int) id);
                a.setNomPlat(obj.get("nomplat").toString());
                a.setPrixPlat(Double.parseDouble(obj.get("prixplat").toString()));

                // float cat = Float.parseFloat(obj.get("catAge").toString());
                Plat.add(a);

            }

        } catch (IOException ex) {
            ex.printStackTrace();

        }
        return Plat;
    }

    public ArrayList<Plat> AfficheUsers() {
        ArrayList<Plat> list = new ArrayList<>();

        String Url = "http://127.0.0.1:8000/platmobile";
        req.setUrl(Url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                Plat = parsePlat(new String(req.getResponseData()));
                req.removeResponseListener(this);

            }

        });
        req.setFailSilently(true);
        NetworkManager.getInstance().addToQueueAndWait(req);

        return list;
    }
}
