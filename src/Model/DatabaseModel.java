package Model;

import java.sql.*;

/**
 * 邓鹏飞
 * 数据库控制类
 * 化简数据库的操作
 */
public class DatabaseModel {

    private String url = "jdbc:mysql://123.206.49.113:3306/wechat?useUnicode=true&characterEncoding=utf-8";
    private final static String driver = "com.mysql.jdbc.Driver";
    private String userName = "root";
    private String password = "";
    private Connection connection;
    private Statement statement;//静态查询
    private PreparedStatement preparedStatement;//动态查询
    public DatabaseModel() {

    }
    /*
    链接数据库
     */
    public void connect(){
        try {
            Class.forName(driver).newInstance();
            connection = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 邓鹏飞
     *
     * 该方法用来执行Sql语句并返回结果集 适合需要返回结果集的查询语句 例如   execResult("select*from user where id = ? and name = ?","1","jack");
     * 用问号占位 然后传入个String数组代表要问号的值 该方法返回个结果集 即 ResultSet
     *
     * @param Sql
     * @param data
     * @return
     * @throws SQLException
     */
    public  ResultSet execResult(String Sql, String... data) throws SQLException {
        preparedStatement = connection.prepareStatement(Sql);
        for (int i = 1; i <= data.length; i++) {
            preparedStatement.setString(i, data[i - 1]);
        }
        return preparedStatement.executeQuery();
    }

    /**
     * 邓鹏飞
     *
     * 执行Sql语句 不返回任何东西 例如exec("update user set password = ? where account = ?","password","name");
     * exec("delete from user where name = ? and account = ?","name","account");
     * exec("insert into user values(?,?,?,?,?,?,?,?,?)",1,2,3,4,5,6,7,8,9);
     * @param Sql
     * @param data
     * @throws SQLException
     */
    public void exec(String Sql, String...data) throws SQLException {

        preparedStatement = connection.prepareStatement(Sql);
        for (int i = 1; i <= data.length; i++) {
            preparedStatement.setString(i, data[i - 1]);
        }
        preparedStatement.executeUpdate();
    }

    /**
     * 执行静态SQL语句  例如exec("delete from user");
     * @param Sql
     */
    public void exec(String Sql) {
        try
        {
            preparedStatement = connection.prepareStatement(Sql);
            preparedStatement.executeUpdate();
        }catch (Exception e){
        }
    }
    /**
     * 该方法插入个数据  例如insert(表名,要插入的数据(String数组的形式))
     *
     * @param tableName
     * @param data
     * @throws SQLException
     */
    public void insert(String tableName, String... data) throws SQLException {

        String pre = "";
        for (int i = 0; i < data.length; i++) {

            if (i != data.length - 1)
                pre += "?,";
            else
                pre += "?";

        }

        String Sql = "INSERT INTO " + tableName + " VALUES(" + pre + ")";
        preparedStatement = connection.prepareStatement(Sql);
        for (int i = 1; i <= data.length; i++) {

            preparedStatement.setString(i, data[i - 1]);

        }
        preparedStatement.executeUpdate();

    }

    /**
     * 该方法删除表数据 例如delete(表名,删除时的条件(例如"id = ? AND name = ?"),传入问号代表的值)
     *
     * @param tableName
     * @param condition
     * @param data
     * @throws SQLException
     */
    public void delete(String tableName, String condition, String... data) throws SQLException {


        String Sql = "DELETE FROM " + tableName + " WHERE " + condition;


        preparedStatement = connection.prepareStatement(Sql);
        for (int i = 1; i <= data.length; i++) {

            preparedStatement.setString(i, data[i - 1]);


        }
        preparedStatement.executeUpdate();


    }

    /**
     * 跟上面那些一样
     *
     * @param tableName
     * @param target
     * @param condition
     * @param data
     * @throws SQLException
     */
    public void update(String tableName, String target, String condition, String data[]) throws SQLException {
        String Sql = "UPDATE " + tableName + " SET " + target + " WHERE " + condition;
        preparedStatement = connection.prepareStatement(Sql);

        for (int i = 1; i <= data.length; i++) {

            preparedStatement.setString(i, data[i - 1]);

        }
        preparedStatement.executeUpdate();

    }

    /**
     * @param Sql
     * @return
     * @throws SQLException
     */
    public ResultSet select(String Sql) throws SQLException {

        statement = connection.createStatement();
        return statement.executeQuery(Sql);


    }

    /**
     * @param Sql
     * @param data
     * @return
     * @throws SQLException
     */
    public ResultSet select(String Sql, String... data) throws SQLException {


        preparedStatement = connection.prepareStatement(Sql);
        for (int i = 1; i <= data.length; i++) {

            preparedStatement.setString(i, data[i - 1]);
        }
        return preparedStatement.executeQuery();

    }
    /**
     * 王浩东
     *
     * 定义一个静态函数在sql语句中可以直接使用 第一个参数是你给的条件就是 where语句 和更新语句 里面的那些条件
     * 然后第二个参数是 可变长String的长度
     * 这个函数的作用就是 把你给的条件字符串先判断里面有没有age和degree这两个属性 用indexOf 方法 返回的是第一次出现的位置
     * 在遍历的时候用一个sum来记录到第几个？ 然后当找到age后者degree位的时候 把标记位设置为true
     * 然后这样在找到接下来的问号 一定是对应age或者degree后面的 这个时候有一个标志数组 这时候这一位变成 true 再把标记设置为false
     * 这样返回这个标志数组 就知道 你可变长String里面那个是age和degree 因为他们两个是int 的 其他都是String的
     *
     */
    public static boolean[] my_getint(String condition,int n)
    {
        int x1=condition.indexOf("age");
        int x2=condition.indexOf("degree");
        boolean[] flag = new boolean[n];
        if(x1 == -1 && x2 == -1)
        {
            return flag;
        }
        int sum=0;
        boolean my_flag = false;
        for(int i=0; i<condition.length(); i++)
        {
            if(i==x1||i==x2)
            {
                my_flag=true;
            }
            if(condition.charAt(i)=='?')
            {
                if(my_flag)
                {
                    flag[sum]=true;
                    my_flag=false;
                }
                sum++;
            }
        }
        return flag;
    }

    /**
     * 王浩东
     *
     * @param option
     * @param tableName
     * @param condition
     * @param data
     * @return
     * @throws SQLException
     *
     * 第一个参数是你要选出来的选项 比如是name 还是 age 等等
     * 第二个参数是n你要从哪个表里面来选择比如 wechat
     * 第三个参数是条件 比如你要select name from wechat where name = '123' 你需要输入的是 name = ?参数都用问号代替
     * 第四个参数就是你?的数据
     * 返回值是结果集
     * 举例比如查找 账户是123并且密码是234 否存在 sql 语句是 select * from user where name = '123' and password = '234'
     * 你只需要 select (*,user,name= ? and password = ?,"123","234");
     */
    public  ResultSet select(String option,String tableName,String condition,String... data) throws SQLException
    {
        String Sql;
        Sql="select"+option+"from"+tableName+"where"+condition;
        boolean[] flag = new boolean[data.length];
        flag = my_getint(condition,data.length);
        preparedStatement = connection.prepareStatement(Sql);
        for (int i = 1; i <= data.length; i++)
        {
            if(flag[i-1]) preparedStatement.setInt(i, Integer.parseInt(data[i - 1]));
            preparedStatement.setString(i, data[i - 1]);
        }
        return preparedStatement.executeQuery();
    }

    /**
     *王浩东
     * @param tableName
     * @param data
     * @throws SQLException
     *
     * 第一个参数是你要插入的表名 第二个参数是条件
     * sql语句是 加入插入三个数据进入 wechat 表 insert into wecaht value(?,?,?)
     *第三个参数就是 ? 问号对应的参数
     * 第二个参数呢 你在输入的时候和sql 有所不一样
     * 举个例子 在 user 表 插入一个记录 包含账户(account) 密码(password) 性别(sex)
     * sql语句是 insert into user value(?,?,?)
     * 但是你要这么输入
     * insert(user,"account = ? and password = ? and sex = ?","account的值","password的值","sex的值");
     * 类似上面select 查询的条件
     *
     */
    public void insert(String tableName,String condition, String... data) throws SQLException
    {
        String pre = "";
        for (int i = 0; i < data.length; i++)
        {
            if (i != data.length - 1)  pre += "?,";
            else  pre += "?";
        }
        String Sql = "INSERT INTO " + tableName + " VALUES(" + pre + ")";
        preparedStatement = connection.prepareStatement(Sql);
        boolean[] flag = new boolean[data.length];
        flag = my_getint(condition,data.length);
        for (int i = 1; i <= data.length; i++)
        {
            if(flag[i-1]) preparedStatement.setInt(i, Integer.parseInt(data[i - 1]));
            preparedStatement.setString(i, data[i - 1]);
        }
        preparedStatement.executeUpdate();
    }

    /**
     * 得到静态查询对象
     * @return
     */
    public Statement getStatement() {
        return statement;
    }

    /**
     * 得到动态查询对象
     * @return
     */
    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    /**
     * 得到数据库链接对象
     * @return
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * 数据库重连
     * @param Url
     * @param UserName
     * @param Password
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void reConnection(String Url, String UserName, String Password) throws ClassNotFoundException, SQLException {

        Class.forName(driver);
        connection = DriverManager.getConnection(Url, UserName, Password);

    }


}
