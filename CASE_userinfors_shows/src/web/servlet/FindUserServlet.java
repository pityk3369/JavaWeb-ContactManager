package web.servlet;

import domain.User;
import service.UserService;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/findUserServlet")
public class FindUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.获取id
        String id = request.getParameter("id");//request域好像只能为String类型，所以下面需要强制类型转换
        //2.调用Servlet查询
        UserService service = new UserServiceImpl();
        User user = service.findUserById(id);

        //3.将user存入request
        request.setAttribute("user", user);
        //4.转发到update.jsp
        request.getRequestDispatcher("/update.jsp").forward(request, response);


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
