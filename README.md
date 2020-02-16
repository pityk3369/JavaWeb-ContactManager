# 今日内容

1. 综合练习
   1. 简单功能
      1. 列表查询
      2. 登录

      3. 添加

      4. 删除
      5. 修改

   2. 复杂功能
      1. 删除选中
      2. 分页查询
         * 好处：
           1. 减轻服务器内存的开销
           2. 提升用户体验
      3. 复杂条件查询

## 1-界面展示

   ![image](https://github.com/pityk3369/JavaWeb-ContactManager/blob/master/image-20200214110816360.png)

- 之前由于未加入CSS文件夹下的内容，网页格式完全错误！加入css文件后正常！

  ![image](https://github.com/pityk3369/JavaWeb-ContactManager/blob/master/image-20200214110907532.png)

- ![image](https://github.com/pityk3369/JavaWeb-ContactManager/blob/master/image-20200214111143859.png)

  界面终于好了！

- ```jsp
  <div class="container">
      <h3 style="text-align: center">用户信息列表</h3>
  
      <div style="float: left;">	<!--内联表单左对齐-->
          <form class="form-inline">	<!--使用内联表单样式-->
              <div class="form-group">
                  <label for="exampleInputName2">姓名</label>
                  <input type="text" name="name" class="form-control" id="exampleInputName2" >
              </div>
          </form>
  
      </div>
  
      <div style="float: right;margin: 5px;">	
      <!-- 添加、删除按钮右对齐；使得二者在同一行内左右而立-->
          <a class="btn btn-primary" href="${pageContext.request.contextPath}/add.jsp">添加联系人</a>
          <a class="btn btn-primary" href="add.html">删除选中</a>
  
      </div>
  
      <table border="1" class="table table-bordered table-hover">
      <!-- 表格-带边框且边框为1个像素 带鼠标悬停 -->
          <tr class="success">
              <th><input type="checkbox"></th>
              <th>编号</th>
              <th>姓名</th>
              <th>性别</th>
              <th>年龄</th>
              <th>籍贯</th>
              <th>QQ</th>
              <th>邮箱</th>
              <th>操作</th>
          </tr>
      </table>
      </div>
  
  </div>
  
  ```

  


## 2-登录

1. 调整页面，加入验证码功能

  ![image](https://github.com/pityk3369/JavaWeb-ContactManager/blob/master/image-20200213171504124.png)

2. 代码实现

## 3-添加联系人

**增加按钮--新增add页面并连接action--新增AddUserServlet.java方法 -- UserService中增加adduser服务 -- 在UserDao中新增adduser操作数据库**

1. 在list.jsp内修改：

   ```jsp
   <a class="btn btn-primary" href="${pageContext.request.contextPath}/add.jsp">添加联系人</a>
   ```

2. 新增add.jsp文件，并修改其中主要的action：

   ```jsp
   <center><h3>添加联系人页面</h3></center>
       <form action="${pageContext.request.contextPath}/addUserServlet" method="post">
   ```

3. 新增AddUserServlet.java文件

   - 设置编码、获取request参数、封装对象(捕获异常)、

     ```java
     //1.设置编码
     request.setCharacterEncoding("utf-8");
     //2.获取参数
     Map<String, String[]> map = request.getParameterMap();
     //3.封装对象
     User user = new User();
     try {
         BeanUtils.populate(user,map);
     } catch (IllegalAccessException e) {
         e.printStackTrace();
     } catch (InvocationTargetException e) {
         e.printStackTrace();
     }
     ```

   - 调用Service保存User、跳转到userlistServlet

     ```java
     //4.调用Service保存
     UserService service = new UserServiceImpl();
     service.addUser(user);
     
     //5.跳转到userListServlet，getContextPath获取到虚拟路径，
     response.sendRedirect(request.getContextPath()+"/userListServlet");
     ```

4. 在UserService中增加方法 addUser(User user)；在UserServiceImpl中重写该方法

   ```java
   @Override
   public void addUser(User user) {
       dao.add(user);
   }
   ```

5. 在UserDao接口中定义add(User user)方法；在UserDaoImpl中重写方法

   ```java
   @Override
       public void add(User user) {
   
           //1.定义sql,ID为null,自增长；其余6个属性为？
           String sql = "insert into user values(null,?,?,?,?,?,?)";
           //2.执行sql
           template.update(sql, user.getName(), user.getGender(), user.getAge(), user.getAddress(), user.getQq(), user.getEmail());
       }
   ```



## 4-删除联系人

点击删除，直接进入DelUserServlet程序，绑定参数：这条记录的id。

- DelUserServlet：

  获取参数：用户id；调用service删除；跳转查询所有的Servlet。

- UserService：

  ```
  public void deleteUser(String id){
  	dao.delete(Integer.parseInt(id));
  }
  ```

- UserDao：

  ```
  public void delete(int id){
  	操作数据库删除
  }
  ```

  

- 在list.jsp中，修改删除的href

  ```jsp
  <td><a class="btn btn-default btn-sm" href="update.html">修改</a>&nbsp;
                      <a class="btn btn-default btn-sm" href="${pageContext.request.contextPath}/delUserServlet?id=${user.id}">删除</a>
  </td>
                  
  ```

- 新增DelUserServlet.java

  ```java
  //1.获取id
  String id = request.getParameter("id");
  //2.调用service删除
  UserService service = new UserServiceImpl();
  service.deleteUser(id);
  
  //3.跳转到查询所有Servlet
  response.sendRedirect(request.getContextPath() + "/userListServlet");
  ```

- 在UserService接口中定义deleteUser方法；在UserServiceImpl重写deleteUser方法；

  ```java
  /**
  * 根据id来删除User
  * @param id
  */
  void deleteUser(String id);
  
  @Override
  public void deleteUser(String id) {
      dao.delete(Integer.parseInt(id));
  }
  ```

- 在UserDao接口中定义delete方法；在UserDaoImpl重写delete方法；

  ```java
  void delete(int parseInt);
  
  @Override
  public void delete(int id) {
      //1.定义sql
      String sql = "delete from user where id = ?";
      //2.执行sql
      template.update(sql, id);
  }
  
  ```

- 至此！删除功能完善好了，但是出现的问题就是：如果误点删除呢？都不出现，确认删除的弹窗吗？那就修改一下“删除”按钮后的href吧！

  修改list.jsp中删除按钮的href如下；并在list头部添加script定义；

  ```jsp
  <a class="btn btn-default btn-sm" href="javascript:deleteUser(${user.id});">删除</a></td>
  
  <script>
      function deleteUser(id) {
          //用户安全提示
          if (confirm("您确定要删除吗？")) {
              //访问路径
              location.href = "${pageContext.request.contextPath}/delUserServlet?id=" + id;
          }
  
      }
  </script>
  ```



## 5-修改联系人

![image](https://github.com/pityk3369/JavaWeb-ContactManager/blob/master/image-20200214142529736.png)

- 新增update.jsp文件

  在修改联系人页面内，潜在一个隐藏域，用来传递联系人id。

  ```jsp
  <div class="container" style="width: 400px;">
      <h3 style="text-align: center;">修改联系人</h3>
      <form action="${pageContext.request.contextPath}/updateUserServlet" method="post">
          <!-- 隐藏域 提交id -->
          <input type="hidden" name="id" value="${user.id}">
  
      </form>
  </div>
  ```

- 新增FindUserServlet

  ```java
  //1.获取id
  String id = request.getParameter("id");//request域好像只能为String类型，所以下面需要强制类型转换
  //2.调用Servlet查询
  UserService service = new UserServiceImpl();
  User user = service.findUserById(id);
  
  //3.将user存入request
  request.setAttribute("user", user);
  //4.转发到update.jsp
  request.getRequestDispatcher("/update.jsp").forward(request, response);
  ```

- UserService接口内定义User findUserById(String id)；UserServiceImpl内重写该方法：

  ```java
  @Override
  public User findUserById(String id) {
      return dao.findUserById(id);
  }
  ```

- UserDao接口内定义User findUserById(String id)；UserDaoImpl内重写该方法：

  ```java
  @Override
  public User findUserById(String id) {
      String sql = "select * from user where id=?";
  
      return template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), id);
  }
  ```

- update.jsp文件内修改页面显示情况

  - 首先要对页面的信息进行回显，在此基础之上完成信息修改。

    传入value的值value="${user.属性名称}"；性别判断后用checked选中；籍贯判断后用selected选中。

    ```jsp
    <div class="form-group">
        <label for="name">姓名：</label>
        <input type="text" class="form-control" id="name" value="${user.name}" name="name"  readonly="readonly" placeholder="请输入姓名" />
    </div>
    
    <div class="form-group">
        <label>性别：</label>
        <c:if test="${user.gender == '男'}">
            <input type="radio" name="gender" value="男" checked />男
            <input type="radio" name="gender" value="女"  />女
        </c:if>
    
        <c:if test="${user.gender == '女'}">
            <input type="radio" name="gender" value="男"  />男
            <input type="radio" name="gender" value="女" checked  />女
        </c:if>
    
    
    </div>
    
    <div class="form-group">
        <label for="age">年龄：</label>
        <input type="text" class="form-control" id="age"  value="${user.age}" name="age" placeholder="请输入年龄" />
    </div>
    
    <div class="form-group">
        <label for="address">籍贯：</label>
        <select name="address" id="address" class="form-control" >
            <c:if test="${user.address == '陕西'}">
                <option value="陕西" selected>陕西</option>
                <option value="北京">北京</option>
                <option value="上海">上海</option>
            </c:if>
    
            <c:if test="${user.address == '北京'}">
                <option value="陕西" >陕西</option>
                <option value="北京" selected>北京</option>
                <option value="上海">上海</option>
            </c:if>
    
            <c:if test="${user.address == '上海'}">
                <option value="陕西" >陕西</option>
                <option value="北京">北京</option>
                <option value="上海" selected>上海</option>
            </c:if>
        </select>
    </div>
    
    <div class="form-group">
        <label for="qq">QQ：</label>
        <input type="text" id="qq" class="form-control" value="${user.qq}" name="qq" placeholder="请输入QQ号码"/>
    </div>
    
    <div class="form-group">
        <label for="email">Email：</label>
        <input type="text" id="email" class="form-control" value="${user.email}" name="email" placeholder="请输入邮箱地址"/>
    </div>
    
    <div class="form-group" style="text-align: center">
        <input class="btn btn-primary" type="submit" value="提交" />
        <input class="btn btn-default" type="reset" value="重置" />
        <input class="btn btn-default" type="button" value="返回"/>
    </div>
    ```

  

- 新增UpdateUserServlet

  ```java
  //1.设置编码
  request.setCharacterEncoding("utf-8");
  //2.获取map
  Map<String, String[]> map = request.getParameterMap();
  //3.封装对象
  User user = new User();
  try {
      BeanUtils.populate(user, map);
  } catch (IllegalAccessException e) {
      e.printStackTrace();
  } catch (InvocationTargetException e) {
      e.printStackTrace();
  }
  //4.调用Service修改
  UserService service = new UserServiceImpl();
  service.updateUser(user);
  //5.跳转到查询所有Servlet
  response.sendRedirect(request.getContextPath() + "/userListServlet");
  ```

- 在UserService接口中添加update()方法；在UserServiceImpl重写update方法：

  ```java
  @Override
  public void updateUser(User user) {
      dao.update(user);
  
  }
  ```

- 在UserDao接口中添加update方法；在UserDaoImpl重写该方法：

  ```java
  @Override
  public void update(User user) {
      String sql = "update user set name = ?,gender = ? ,age = ? , address = ? , qq = ?, email = ? where id = ?";
      template.update(sql, user.getName(), user.getGender(), user.getAge(), user.getAddress(), user.getQq(), user.getEmail(), user.getId());
  
  }
  ```

- 修改联系人界面设置完毕了！提交修改可以完成，但是进入修改页面后，点击“返回”却无任何反应，百度后发现，返回按钮需要响应一个事件：

  ```jsp
  <div class="form-group" style="text-align: center">
      <input class="btn btn-primary" type="submit" value="提交" />
      <input class="btn btn-default" type="reset" value="重置" />
      <input class="btn btn-primary" type="button" value="返回" onclick="javascript:history.go(-1)"/>
  </div>
  ```



## 6-删除选中的联系人

- 声明1个表单项，将table填入，该表单项内只有一个input输入：
  <th><input type="checkbox" name="uid"></th>

- 给删除选中按钮绑定单击事件！

  ```js
  //给上面这个表单项命名叫：form
  <form id="form" action="${pageContext.request.contextPath}/delSelectServlet" method="post">
  
  //等待页面事件加载完毕后运行该事件
  window.onload = function () {
      //给删除选中按钮添加单击事件
      document.getElementById("delSelected").onclick = function () {
          //表单提交
          document.getElementById("form").submit();
      };
  
  };
  ```

- 通过浏览器抓包来查看form表单提交情况

  选中联系人，勾选复选框
  ![image](https://github.com/pityk3369/JavaWeb-ContactManager/blob/master/image-20200214204649792.png)

  查看表单提交的情况：

  ![image](https://github.com/pityk3369/JavaWeb-ContactManager/blob/master/image-20200214204526205.png)

  

- 新增DelSelectedServlet，

  ```java
  //1.获取所有id
  String[] ids = request.getParameterValues("uid");
  //2.调用service
  UserService service = new UserServiceImpl();
  service.delSerlectedUser(ids);
  
  //3.跳转查询所有的Servlet
  //因为网页之间不存在数据共享，故可使用重定向。
  response.sendRedirect(request.getContextPath() + "/userListServlet");
  ```

- 在UserService接口中定义delSerlectedUser方法；并在UserServiceImpl重写该方法：

  ```java
  /**
   * 批量删除选中用户
   * @param ids
   */
  @Override
  public void delSerlectedUser(String[] ids) {
      //1.遍历数组
      for (String id : ids) {
          //2.调用dao删除
          dao.delete(Integer.parseInt(id));
      }
  }
  ```

- 由于循环遍历调用了dao删除用户信息，故不用再对dao修改！

- 完善细节1：点击删除选中，弹出确认方框！

  ```js
  //修改list.jsp中java script代码
  document.getElementById("delSelected").onclick = function () {
      if (confirm("您确定要删除选中条目吗？")) {
          var flag = false;
          //判断是否有条目被选中
          var cbs = document.getElementsByName("uid");
          for (var i = 0; i < cbs.length; i++) {
              if (cbs[i].checked) {
                  //存在条目被选中
                  flag = true;
                  break;
              }
  
          }
          if (flag) {
              //表单提交
              document.getElementById("form").submit();
          }
  
      }
  
  };
  
  ```

  

- 完善细节2：点击全选方框，完成全选操作！

  ```js
  //在表单第1行修改代码
  <tr class="success">
      <th><input type="checkbox" id="firstCb"></th>
      <th>编号</th>
      <th>姓名</th>
      <th>性别</th>
      <th>年龄</th>
      <th>籍贯</th>
      <th>QQ</th>
      <th>邮箱</th>
      <th>操作</th>
  </tr>
  
  //在list.jsp的java script中等待页面加载完毕后运行事件
  //1.获取第一个cb
  document.getElementById("firstCb").onclick = function () {
      //2.获取下边列表中的所有cb
      var cbs = document.getElementById("uid");
      //3.遍历
      for (var i = 0; i < cbs.length; i++) {
          //4.为这些cbs[i]设置checked状态
          cbs[i].checked = this.checked;
      }
  
  };
  ```


## 7-分页查询功能

![image](https://github.com/pityk3369/JavaWeb-ContactManager/blob/master/image-20200215114512934.png)

### 服务器端

> 定义PageBean分页对象，作为输出，给客户端
>
> int totalcount;
> 	//总记录数
> int totalPage;
> 	//总页码=总记录数%每页显示条数==0?总记录数/每页显示条数:总记录数/每页显示条数+1
> List list；			//每页的数据list集合
> int currentPage;//当前页码
> int rows;			//每页显示的条数

### 客户端

> 定义SQL语句：totalCount = select count(*) from user;
> totalPage = 提供每页显示条数给服务器：rows
> list = select * from user limit ?,?;
> 		//第1个？：开始查询的索引；
> 		//第2个?：rows每页显示的条数。
> currentPage = 提供当前页码给服务器
> 开始查询的索引 = (currentPage-1) * rows;

### 在domain包内新建PageBean对象

```java
/**
 * 分页对象，这里的分页，在许多地方都有应用，故加上泛型增加通用性！使用时，初始化泛型即可！
 */
public class PageBean<T> {
    private int totalCount; // 总记录数
    private int totalPage ; // 总页码
    private List<T> list ; // 每页的数据
    private int currentPage ; //当前页码
    private int rows;//每页显示的记录数
    //生成get、set、toString方法
    }
```

### 前端后台调用

根据前端当前页码和每页显示条目数

--》调用FindUserByPageServlet；
--》在UserService中1.新建PageBean对象，2.设置当前页面currentPage属性和rows属性并计算总页码、3.调用dao查询总记录、4.查询list集合；
--》在UserDao中操作数据库：查询总记录数、计算分页查询List，返回给UserService。

![image](https://github.com/pityk3369/JavaWeb-ContactManager/blob/master/image-20200215121105114.png)

- 新增FindUserByPageServlet

  ```java
  //1.获取参数
  String currentPage = request.getParameter("currentPage");
  String rows = request.getParameter("rows");
  
  //2.调用Service查询
  UserServiceImpl service = new UserServiceImpl();
  PageBean<User> pb = service.findUserByPage(currentPage, rows);//泛型实例化
  
  //3.将PageBean存入request
  request.setAttribute("pb", pb);
  //4.转发到list.jsp
  request.getRequestDispatcher("/list.jsp").forward(request, response);
  ```

- UserService中声明接口，并在UserServiceImpl中重写方法：

  ```java
  findUserByPage(currentPage, rows);
  
  public PageBean<User> findUserByPage(String _currentPage, String _rows) {
          int currentPage = Integer.parseInt(_currentPage);
          int rows = Integer.parseInt(_rows);
          if (currentPage <= 0) {
              currentPage = 1;
          }
          //1.创建空的PageBean对象
          PageBean<User> pb = new PageBean<User>();
          //2.设置参数
          pb.setCurrentPage(currentPage);
          pb.setRows(rows);
          //3.调用dao查询总记录数
          int totalCount = dao.findTotalCount(condition);
          pb.setTotalCount(totalCount);
          //4.调用dao查询list集合
          //计算本页开始的记录索引
          int start = (currentPage - 1) * rows;
          List<User> list = dao.findByPage(start, rows);
          pb.setList(list);
          //5.计算总页码
          int totalPage = (totalCount % rows) == 0 ? totalCount / rows : (totalCount / rows) + 1;
          pb.setTotalPage(totalPage);
          return pb;
      }
  ```

- 在UserDao接口中声明findTotalCount(condition);和findByPage(start, rows)方法；并在UserDaoImpl中重写该方法

  ```java
  @Override
  public List<User> findByPage(int start, int rows) {
      String sql = "select * from user limit ?,?";
  
      return template.query(sql, new BeanPropertyRowMapper<User>(User.class), start, rows);
  
  }
  
  @Override
  public int findTotalCount() {
      String sql = "select count(*) from user";
      return template.queryForObject(sql, Integer.class);
  }
  
  ```

- Error running Tomcat 8.5.31：Address localhost: 1099 is already in use

  ```
  >netstat -ano | find "1099"
  找到pid，在任务管理器中结束该进程即可！
  ```

- FindUserByPageServlet中增加"System.out.println(pb);"语句进行测试：

  ```
  http://localhost:8080/userinfors_shows/findUserByPageServlet?currentPage=1&rows=5
  访问页面，
  观察FindUserByPageServlet中输出：PageBean{totalCount=13, totalPage=3, list=[domain.User@1fafe872, domain.User@c07a2d0, domain.User@103a13f9, domain.User@626dad67, domain.User@2613520], currentPage=1, rows=5}
  
  数据输出正确，但是list输出不规范！观察User类中toString方法声明不正确！
  @Override
  public String toString() {
      return super.toString();
  }
  
  ```

  

- 编写前端代码

  ```
  修改list.jsp中：<c:forEach items="${users}" var="user" varStatus="s"> 为
  <c:forEach items="${pb.list}" var="user" varStatus="s">
  刷新页面：
  发现联系人列表出现！
  ```

  - 修改页码总记录

    ![image](https://github.com/pityk3369/JavaWeb-ContactManager/blob/master/image-20200215142911425.png)

    ```jsp
    list.jsp中修改：
    <span style="font-size: 25px;margin-left: 5px;">
        共16条记录，共4页
    </span>
     为：
    <span style="font-size: 25px;margin-left: 5px;">
        共${pb.totalCount}条记录，共${pb.totalCount}页
    </span>
    
    修改页码总数显示：
    <c:forEach begin="1" end="${pb.totalPage}" var="i">
        <li><a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${i}&rows=5">${i}</a></li>
    
    </c:forEach>
    这样的目的是：每个页码显示的数字都会是一个跳转页码链接，点击后可直接跳转过去！
    
    ```

  - 高亮当前页面标记：

    ```
    <c:forEach begin="1" end="${pb.totalPage}" var="i">
      <c:if test="${pb.currentPage==i}">
            <li class="active">
                <a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${i}&rows=5">${i}</a>
            </li>
        </c:if>
        <c:if test="${pb.currentPage!=i}">
        <li >
            <a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${i}&rows=5">${i}</a>
        </li>
        </c:if>
    </c:forEach>
    ```

    ​    

  - 修改主页跳转链接

    ```jsp
    <a
    href="${pageContext.request.contextPath}/findUserByPageServlet" style="text-decoration:none;font-size:33px">查询所有用户信息
    </a>
    
    并在findUserByPageServlet代码中给出默认的当前页面和页面显示条目设置：
    if (currentPage == null || "".equals(currentPage)) {
        currentPage = "1";
    }
    if (rows == null || "".equals(rows)) {
        rows = "5";
    }
    ```

  

  - 设置翻页按钮的上下界限：

    ```java
    /*UserServiceImpl的findUserByPage方法里：
    在调用pb.setCurrentPage(currentPage);前进行判断：*/
    if (currentPage <= 0) {
        currentPage = 1;
    }
    //在计算完最大页码后，进行判断，当前页码的重新赋值：
    pb.setTotalPage(totalPage);
    if (currentPage >= totalPage) {
        pb.setCurrentPage(totalPage);
    }
    
    在list.jsp文件中修改：
    <c:if test="${pb.currentPage == 1}">
        <li class="disabled">
            <a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${1}&rows=5"
               aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
            </a>
        </li>
    </c:if>
    
    <c:if test="${pb.currentPage != 1}">
        <li>
            <a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${pb.currentPage-1}&rows=5"
               aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
            </a>
        </li>
    </c:if>
    
    <c:if test="${pb.currentPage == pb.totalPage}">
        <li class="disabled">
            <a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${pb.totalPage}&rows=5"
               aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
            </a>
        </li>
    </c:if>
    
    <c:if test="${pb.currentPage != pb.totalPage}">
        <li>
            <a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${pb.currentPage+1}&rows=5"
               aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
            </a>
        </li>
    </c:if>
    ```



## 8-复杂条件查询

MYSQL语句为：

```mysql
SELECT * FROM USER WHERE NAME LIKE '%李' AND address LIKE '%北京%' LIMIT 0,5;
```

![image](https://github.com/pityk3369/JavaWeb-ContactManager/blob/master/image-20200215171134200.png)

- 还是利用PageBean，从后台输出前端；从前端的条件筛选表单建立个map传回参数。
  根据map中的value值动态拼接生成sql语句；
  1.定义初始化sql语句：select count(*) from user where 1=1
  2.遍历map，判断其value是否有值：sb.append("and key like ? ")

- 表单的获取在list.jsp中：增加action和post方法

  ```jsp
  <form class="form-inline" action="${pageContext.request.contextPath}/findUserByPageServlet" method="post">
      <div class="form-group">
          <label for="exampleInputName2">姓名</label>
          <input type="text" name="name" class="form-control" id="exampleInputName2">
      </div>
      <div class="form-group">
          <label for="exampleInputName3">籍贯</label>
          <input type="text" name="address" class="form-control" id="exampleInputName3">
      </div>
  
      <div class="form-group">
          <label for="exampleInputEmail2">邮箱</label>
          <input type="text" name="email" class="form-control" id="exampleInputEmail2">
      </div>
      <button type="submit" class="btn btn-default">查询</button>
  </form>
  
  对于邮箱条件的查询，避免邮箱格式的硬性规定！将其type="email"修改为type="text"!
  ```

- 在FindUserByPageServlet中修改：增加条件查询参数，service.findUserByPage方法中增加condition参数
  condition参数中：key=name、addre、email，其对应的value是个字符串数组，但是值都是第一个值

  ```java
  //获取list.jsp中条件查询参数
  Map<String, String[]> condition = request.getParameterMap();
  
  //2.调用Service查询
  UserServiceImpl service = new UserServiceImpl();
  PageBean<User> pb = service.findUserByPage(currentPage, rows,condition);//泛型实例化
  
  修改UserService接口的定义：
  PageBean<User> findUserByPage(String _currentPage, String _rows, Map<String, String[]> condition);
  
  修改UserServiceImpl中方法调用：
  //3.调用dao查询总记录数
  int totalCount = dao.findTotalCount(condition);
  pb.setTotalCount(totalCount);
  
  //4.调用dao查询list集合
  //计算本页开始的记录索引
  int start = (currentPage - 1) * rows;
  List<User> list = dao.findByPage(start, rows, condition);
  pb.setList(list);
  
  ```

- 在UserDao接口中修改方法定义，在UserDaoImpl中重写该方法：

  ```java
  @Override
  public int findTotalCount(Map<String, String[]> condition) {
      //1.d定义模板初始化sql
      String sql = "select count(*) from user where 1=1 ";
      StringBuilder sb = new StringBuilder(sql);
      //2.遍历map
      Set<String> keySet = condition.keySet();
      //定义参数的集合
      List<Object> params = new ArrayList<Object>();
  
      for (String key : keySet) {
          //获取value
          String value = condition.get(key)[0];//获取首个值
          if (value != null && !"".equals(value)) {
              //有值
              sb.append(" and " + key + " like ? ");
              params.add("%"+value+"%");//sql语句中，？的值
          }
      }
      System.out.println(sb.toString());
      System.out.println(params.toArray());
  
      return template.queryForObject(sb.toString(), Integer.class, params.toArray());
  }
  ```



- 重写了UserDaoImpl的findTotalCount方法后，总记录数和总页码数正确，下面来修改下查询结果页面显示，重写findByPage方法

  ```java
  @Override
  public List<User> findByPage(int start, int rows, Map<String, String[]> condition) {
      String sql = "select * from user  where 1 = 1 ";
  
      StringBuilder sb = new StringBuilder(sql);
      //2.遍历map
      Set<String> keySet = condition.keySet();
      //定义参数的集合
      List<Object> params = new ArrayList<Object>();
      for (String key : keySet) {
  
          //排除分页条件参数
          if("currentPage".equals(key) || "rows".equals(key)){
              continue;
          }
  
          //获取value
          String value = condition.get(key)[0];
          //判断value是否有值
          if(value != null && !"".equals(value)){
              //有值
              sb.append(" and "+key+" like ? ");
              params.add("%"+value+"%");//？条件的值
          }
      }
  
      //添加分页查询
      sb.append(" limit ?,? ");
      //添加分页查询参数值
      params.add(start);
      params.add(rows);
      sql = sb.toString();
      System.out.println(sql);
      System.out.println(params);
  
      return template.query(sql,new BeanPropertyRowMapper<User>(User.class),params.toArray());
  }
  ```



- 增加查询条件的回显，增加每一个条件的value值，并在findUserByPageServlet中增加“//查询记录回显的maprequest.setAttribute("condition", condition);”语句；

  ```
  <form class="form-inline" action="${pageContext.request.contextPath}/findUserByPageServlet" method="post">
      <div class="form-group">
          <label for="exampleInputName2">姓名</label>
          <input type="text" name="name" value="${condition.name[0]}" class="form-control" id="exampleInputName2">
      </div>
      <div class="form-group">
          <label for="exampleInputName3">籍贯</label>
          <input type="text" name="address" value="${condition.address[0]}" class="form-control" id="exampleInputName3">
      </div>
      <div class="form-group">
          <label for="exampleInputEmail2">邮箱</label>
          <input type="text" name="email" value="${condition.email[0]}" class="form-control" id="exampleInputEmail2">
      </div>
      <button type="submit" class="btn btn-default">查询</button>
  </form>
  ```



- 存在一个bug：筛选条件查询后，页码跳转后，筛选条件失效！

  解决：将筛选条件加入到网页request链接中！

  ```jsp
  <c:if test="${pb.currentPage == pb.totalPage}">
      <li class="disabled">
          <a href="${pageContext.request.contextPath}/findUserByPageServlet?currentPage=${pb.totalPage}&rows=5&name=${condition.name[0]}&address=${condition.address[0]}&email=${condition.email[0]}"
             aria-label="Next">
              <span aria-hidden="true">&raquo;</span>
          </a>
      </li>
  </c:if>
  主要就是：&name=${condition.name[0]}&address=${condition.address[0]}&email=${condition.email[0]}
  ```

  





  
