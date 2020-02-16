package web.servlet;

import domain.PageBean;
import domain.User;
import service.UserService;
import service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/findUserByPageServlet")
public class FindUserByPageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        //1.获取参数
        String currentPage = request.getParameter("currentPage");
        String rows = request.getParameter("rows");

        if (currentPage == null || "".equals(currentPage)) {
            currentPage = "1";
        }
        if (rows == null || "".equals(rows)) {
            rows = "5";
        }

        //获取list.jsp中条件查询参数
        Map<String, String[]> condition = request.getParameterMap();

        //2.调用Service查询
        UserServiceImpl service = new UserServiceImpl();
        PageBean<User> pb = service.findUserByPage(currentPage, rows,condition);//泛型实例化

        //toString方法
        System.out.println(pb);

        //3.将PageBean存入request
        request.setAttribute("pb", pb);
        //查询记录回显的map
        request.setAttribute("condition", condition);
        //4.转发到list.jsp
        request.getRequestDispatcher("/list.jsp").forward(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
