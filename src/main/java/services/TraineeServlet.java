package services;


import com.google.gson.Gson;
import dao.TraineeDao;
import model.Trainee;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/trainee/*")
public class TraineeServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final TraineeDao traineeDao = new TraineeDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try(BufferedReader reader = req.getReader()){
            Trainee t = gson.fromJson(reader, Trainee.class);

            if (t == null){
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Invalid Trainee\"}");
                return;
            }
            boolean isSaved= traineeDao.addTrainee(t);
            System.out.println(isSaved);

            if(isSaved){
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"message\":\"Trainee added Successfully\"}");
            }else{
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\":\"Failed to insert}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Invalid JSON:"+ e.getMessage() + "\"}");
        }
//        Trainee t = gson.fromJson(req.getReader(),Trainee.class);
//        boolean added = traineeDao.addTrainee(t);
//        resp.setContentType("application/json");
//        resp.setStatus(added ? 201 :500);
//        resp.getWriter().write(added ? "{\"message\": \"Trainee added\"}":"{\"error\":\"Failed to add trainee\"}");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String idParam = req.getParameter("id");
        if (idParam !=null){
            int id = Integer.parseInt(idParam);
            Trainee t = traineeDao.getTraineeById(id);
            if(t != null){
                resp.getWriter().write(gson.toJson(t));
            }else {
                resp.setStatus(404);
                resp.getWriter().write("{\"error\":\"Trainee not found\"}");
            }
        }else {
            List<Trainee> list=traineeDao.getAllTrainees();
            resp.getWriter().write(gson.toJson(list));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (BufferedReader reader = req.getReader()){
        Trainee trainee = gson.fromJson(reader,Trainee.class);

            if (trainee.getId() == 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Missing trainee id\"}");
                return;
            }

            boolean isUpdated = traineeDao.updateTrainee(trainee);
            if (isUpdated){
                resp.getWriter().write("{\"message\":\"Trainee updated successfully\"}");
            }else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Trainee not found\"}");
            }

        } catch (Exception e) {
           resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
           resp.getWriter().write("{\"error\":\"Invalid JSON:" + e.getMessage() + "\"}");
        }

//        Trainee t = gson.fromJson(req.getReader(),Trainee.class);
//        boolean updated = traineeDao.updateTrainee(t);
//        resp.setContentType("application/json");
//        resp.setStatus(updated?200:404);
//        resp.getWriter().write(updated ? "{\"message\":\"Trainee updated\"}":"{\"error\":\"Trainee not found\"}");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID is required to delete\"}");
            return;
        }
        try{
            int id = Integer.parseInt(pathInfo.substring(1));
            boolean isDeleted = traineeDao.deleteTrainee(id);

            if (isDeleted) {
                resp.getWriter().write("{\"message\":\"Trainee deleted successfully\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Trainee not found\"}");
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid Format\"}");
        }
//        int id = Integer.parseInt(req.getParameter("id"));
//        boolean deleted = traineeDao.deleteTrainee(id);
//        resp.setContentType("application/json");
//        resp.setStatus(deleted?200:404);
//        resp.getWriter().write(deleted ? "{\"message\":\"Trainee deleted\"}":"{\"error\":\"Trainee not found\"}");
    }

   }

