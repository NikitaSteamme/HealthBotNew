package com.company.Engine;


import com.company.Entitys.Conection;
import com.company.Entitys.Disease;
import com.company.Entitys.Symptom;
import com.company.Listeners.LoginDiseaseActionListener;
import com.company.Listeners.LoginSymptomActionListener;
import com.company.utils.HibernateUtil;
import org.hibernate.Session;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class HealthEngine {

    public void addDisease() throws SQLException {
        HibernateUtil.init();
        Disease disease = new Disease();
        disease.setDiseaseName(LoginDiseaseActionListener.win.getDiseaseTextField().getText());
        disease.setDiseaseNameSynonyms(LoginDiseaseActionListener.win.getDiseaseNameSynonymsfield().getText());
        disease.setSpecialistType(LoginDiseaseActionListener.win.getDiseaseSpecialistTypeField().getText());
        DiseaseDAOimpl diseaseDAOimpl = new DiseaseDAOimpl();
        diseaseDAOimpl.addDisease(disease);
        String id = Long.toString(disease.getDiseaseID());
        LoginDiseaseActionListener.win.getDeseaseIDtextField().setText(id);

    }

    public void delDiseaseById() {
        HibernateUtil.init();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM Disease where diseaseID = " + LoginDiseaseActionListener.win.getDeseaseIDtextField().getText()).executeUpdate();
        session.getTransaction().commit();
    }

    public void addSymptom() throws SQLException {
        HibernateUtil.init();
        Symptom symptom = new Symptom();
        symptom.setSymptomName(LoginSymptomActionListener.winSymptom.getSymptomNameTextField().getText());
        symptom.setSymptomSynonyms(LoginSymptomActionListener.winSymptom.getSymptomNameSynonymsfield().getText());
        SymptomDAOimpl symptomDAOimpl = new SymptomDAOimpl();
        symptomDAOimpl.addSymptom(symptom);
        String id = Long.toString(symptom.getSymptomId());
        LoginSymptomActionListener.winSymptom.getSymptomIdTextField().setText(id);
    }

    public void delSymptomById() {
        HibernateUtil.init();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM Symptom where symptomId = " + LoginSymptomActionListener.winSymptom.getSymptomIdTextField().getText()).executeUpdate();
        session.getTransaction().commit();
    }

    public void addConection() throws SQLException {
        Symptom symptom = null;
        HibernateUtil.init();
        Conection conection = new Conection();
        Integer i1 = new Integer(LoginDiseaseActionListener.win.getDeseaseIDtextField().getText());
        conection.setDiseaseid(i1);
        SymptomDAOimpl symptomDAOimpl = new SymptomDAOimpl();
        symptom = symptomDAOimpl.getSymptomByName(LoginDiseaseActionListener.win.getSymptomForConectTextField().getText());
        conection.setSymptomid((int) symptom.getSymptomId());
        ConectionDAOimpl conectionDAOimpl = new ConectionDAOimpl();
        conectionDAOimpl.addConection(conection);
    }

    public void findUserDisease(String symptomName) {
        Session session = null;
        List t = null;
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            t = session.createQuery("SELECT disease.diseaseName from Symptom " +
                    "INNER JOIN conection " +
                    "ON symptom.symptomid = conection.symptomid " +
                    "INNER JOIN disease " +
                    "ON conection.diseaseid = disease.diseaseID " +
                    "WHERE symptom.symptomName = :symptom_name").setParameter("symptom_name",symptomName).list();
            System.out.println(t.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ошибка I/O", JOptionPane.OK_OPTION);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }


    }

}

