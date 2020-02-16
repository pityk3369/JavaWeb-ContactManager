<%--
  Created by IntelliJ IDEA.
  User: 30480
  Date: 2020/2/12
  Time: 17:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>首页</title>

    <!-- 1. 导入CSS的全局样式 -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- 2. jQuery导入，建议使用1.9以上的版本 -->
    <script src="js/jquery-2.1.0.min.js"></script>
    <!-- 3. 导入bootstrap的js文件 -->
    <script src="js/bootstrap.min.js"></script>
    <script type="text/javascript">
    </script>
</head>
<body>
<div>${user.name},欢迎您！</div>
<div align="center">
    <!-- 点击后实现页面跳转-->
    <%--    <a--%>
    <%--        href="${pageContext.request.contextPath}/findUserByPageServlet" style="text-decoration:none;font-size:33px">查询所有用户信息--%>
    <%--    </a>--%>
    <!-- 5秒后自动跳转到登录界面 -->
    <a style="text-decoration:none;font-size:33px">欢迎登陆！</a>
    <br>
    <a style="text-decoration:none;font-size:20px">5秒后自动跳转到登录界面</a>
    <meta http-equiv="refresh" content="5;url=${pageContext.request.contextPath}/login.jsp">

</div>
</body>
</html>
</div>