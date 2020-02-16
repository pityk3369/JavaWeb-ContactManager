package service;

import domain.PageBean;
import domain.User;

import java.util.List;
import java.util.Map;

/**
 * 用户管理的业务接口
 */
public interface UserService {
    /**
     * 查询所有用户信息
     * @return
     */
    public List<User> findAll();

    /**
     * 登录方法
     * @param user
     * @return
     */
    User login(User user);

    /**
     * 保存User
     * @param user
     */
    void addUser(User user);

    /**
     * 根据id来删除User
     * @param id
     */
    void deleteUser(String id);

    User findUserById(String id);

    void updateUser(User user);

    /**
     * 批量删除选中的用户
     * @param ids
     */
    void delSerlectedUser(String[] ids);

    /**
     * 分页、条件查询
     * @param _currentPage
     * @param _rows
     * @param condition
     * @return
     */
    PageBean<User> findUserByPage(String _currentPage, String _rows, Map<String, String[]> condition);
}
