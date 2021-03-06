package controller;

import common.*;
import model.Customer;
import service.CustomerService;
import service.CustomerServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CustomerServlet", urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {
    private CustomerService customerService = new CustomerServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = " ";
        }
        switch (action) {
            case "create":
                addNewCustomer(request, response);
                break;
            case "edit":
                editCustomer(request, response);
                break;
        }
    }

    private void editCustomer(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String type = request.getParameter("type");
        String fullName = request.getParameter("fullName");
        String birthDay = request.getParameter("birthDay");
        String gender = request.getParameter("gender");
        String idCardNum = request.getParameter("idCardNumber");
        String phoneNumber = request.getParameter("phoneNumber");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        Customer customer = new Customer(id, type, fullName, birthDay, gender, idCardNum, phoneNumber, email, address);
        customerService.editCustomerInformation(customer);
        listCustomer(request, response);
//        RequestDispatcher requestDispatcher = request.getRequestDispatcher("customer/listCustomer.jsp");
//        try {
//            requestDispatcher.forward(request, response);
//        } catch (ServletException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void listCustomer(HttpServletRequest request, HttpServletResponse response) {
        List<Customer> customerList = customerService.getAllCustomer();
        request.setAttribute("customerList", customerList);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("customer/listCustomer.jsp");
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewCustomer(HttpServletRequest request, HttpServletResponse response) {
        Customer customer = null;
        boolean check = true;
        String id = null;
        String type;
        String fullName;
        String birthDay = null;
        String gender;
        String idCardNum = null;
        String phoneNumber = null;
        String email = null;
        String address;
        String errorIdCus = null;
        String errorPhoneNumber = null;
        String errorBirthDay = null;
        String errorIdCard = null;
        String errorEmail = null;
        try {
            id = request.getParameter("id");
            Validate.regexIdCustomer(id);
        } catch (Exception e) {
            errorIdCus = e.getMessage();
            check = false;
        }
        type = request.getParameter("type");
        fullName = request.getParameter("fullName");
        try {
            birthDay = request.getParameter("birthDay");
            Validate.regexBirthDay(birthDay);
        } catch (BirthDayException e) {
            errorBirthDay = e.getMessage();
            check = false;
        }
        gender = request.getParameter("gender");
        try {
            idCardNum = request.getParameter("idCardNumber");
            Validate.regexIdCardNum(idCardNum);
        } catch (IdCardNumberException e) {
            errorIdCard = e.getMessage();
            check = false;
        }

        try {
            phoneNumber = request.getParameter("phoneNumber");
            Validate.regexPhoneNumber(phoneNumber);
        } catch (PhoneNumberException e) {
            errorPhoneNumber = e.getMessage();
            check = false;
        }
        try {
            email = request.getParameter("email");
            Validate.regexEmail(email);
        } catch (EmailException e) {
            errorEmail = e.getMessage();
            check = false;
        }

        address = request.getParameter("address");

        if (!check) {
            request.setAttribute("errorIdCus", errorIdCus);
            request.setAttribute("errorPhoneNumber", errorPhoneNumber);
            request.setAttribute("errorBirthDay", errorBirthDay);
            request.setAttribute("errorIdCard", errorIdCard);
            request.setAttribute("errorEmail", errorEmail);
            request.setAttribute("hasError",check);
            RequestDispatcher requestDispatcher=request.getRequestDispatcher("customer/listCustomer.jsp");
            try {
                requestDispatcher.forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            customer = new Customer(id, type, fullName, birthDay, gender, idCardNum, phoneNumber, email, address);
            customerService.addNewCustomer(customer);
            listCustomer(request, response);
        }


//        RequestDispatcher requestDispatcher = request.getRequestDispatcher("customer/listCustomer.jsp");
//        try {
//            requestDispatcher.forward(request, response);
//        } catch (ServletException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = " ";
        }
        switch (action) {
            case "delete":
                deleteCustomerById(request, response);
                break;
            case "search":
                searchCusByName(request, response);
                break;
            default:
                listCustomer(request, response);
        }
    }

    private void searchCusByName(HttpServletRequest request, HttpServletResponse response) {
        String nameSearch = request.getParameter("name_search");
        List<Customer> customerList = customerService.findByName(nameSearch);
        request.setAttribute("customerList", customerList);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("customer/listCustomer.jsp");
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteCustomerById(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        customerService.deleteCustomer(id);
        listCustomer(request, response);
    }
}
