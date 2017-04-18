## 实训基地管理系统
## API for Front-end Developers
遵循[RESTful](http://baike.baidu.com/link?url=VKqeXMzEAFnL3PYerHNsvr_5zqvcUbrDlCzULXHQBW4sB0Q18xUKoRbqPEP6ppJhcqMpsd8t6TiEmY_y59oi-K)风格，参考[RESTful API 设计指南](http://www.ruanyifeng.com/blog/2014/05/restful_api.html)  
[API.md](/API.md)下载
***
Prerequisite  
* [主要对象](#1) 
* [权限控制](#2) 
* [访问方式](#3)
 
API详情  
* [登录](#5)
* [学生相关](#6)
* [老师相关](#7)
* [项目相关](#8)
* [项目阶段相关](#9)

***
### <h3 id="1">主要对象</h3>

```
ResultBean {  //返回值的统一结构
  code    //错误代码，零为正确，非零表示错误
  msg  //错误信息
  data    //传输的数据。通常，errno!=0时为空，errno==0时为以下数据类型(或其集合)的JSON串
}
```


```
Student {
    username    //not null
    password   //始终为null，无需显示
    name      //not null
    phone
    email
    project   //表示该学生参与的项目，0或1个
}
```
```
Teacher {
  username  //同上
  password  
  name
  phone
  email
  info
  projects   //老师指导的项目，0或多个
}
```
```
Project {
    id
    name
    info
    beginDate
    phases
    students  //项目成员，0或多个
    teacher   //指导教师，有且仅有一个
}
```
```
//项目阶段，一个项目包含0或多个阶段
ProjectPhase {
    projectId
    phaseId
    name
    fileName
}
```
***
### <h3 id="2">用户权限</h3>
1. 管理员：登录，增删学生与老师帐号；
2. 学生：登录，修改个人资料、密码，管理自己所属的项目;
3. 老师：登录，修改个人资料、密码，管理自己创建的项目。

### 身份验证
用户提交用户名，密码登录之后，会返回一个用于验证的字符串，在之后的每次请求头部，必需包含字段auth_token，值为返回的字符串。  

对于缺少或无效的auth_token，会返回一个错误（即code不为零）。
***

### <h3 id="3">访问方式</h3>
API尽量遵循RESTful风格，使用GET，POST，DELETE，PUT，PATCH方法。

其中GET，POST方式正常，其他方法转化成标准的POST方式。

如以PUT访问/student，需转化成以POST访问/student，同时在数据中包含  _method:"PUT"。

```
$("#btnPut").click(function(){
  $.ajax({    
    url: "http://localhost:8080/sxjd-v2/student",
    type: "POST",                                     //标准的POST方式
    headers: {
        "auth_token" : sessionStorage.getItem("auth_token"),
    },

    data: {
      username:$("#username").val(),
      name:$("#name").val(),
      email:$("#email").val(),
      phone:$("#phone").val(),
      _method:'PUT'                         // 在数据中包含_method:"PUT"  
    },

    success: function(data, status) {
      if (data.code != 0) {
        alert("修改失败");
      } else {
        var student = data.data;
        $("#username").val(student.username);
        $("#name").val(student.name);
        $("#email").val(student.email);
        $("#phone").val(student.phone);
        alert("修改成功");
      }
    }
  })
});
```
***

### <h3 id="4">URL，访问方式，参数及其对应的返回值</h3>

#### <h4 id="5">登录</h4>
basePath为  http://172.2.2.91:8080/sxjd-v2

### /login POST
即完整URL为http://172.2.2.91:8080/sxjd-v2/login  下同  
必需参数: username, password, userType(合法字符串"student", "teacher","admin"之一)

返回值
1. 参数缺失时，"参数错误"
```
{
  "code": "2003",
  "msg": "参数错误",
  "data": null
}
```
2. userType错误时，"无效的用户类型"
3. 验证失败时，"验证失败"
4. 成功时返回msg="成功"，data为auth_token
```
{
  "code": "0000",
  "msg": "成功",
  "data": "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJzdHVkZW50IiwiaWF0IjoxNDkxODA1NzM3LCJzdWIiOiIxIiwiaXNzIjoiU1NEVVRfU1hKRCIsImV4cCI6MTQ5MTgwOTMzN30.Nliata5yzpXneQcpKobKvsup6xRi4bfxEajbAAIufr8"
}
```


以下所有请求需携带有效的auth_token头部信息，否则返回错误
```
{
  "code": "1004",
  "msg": "验证失败",
  "data": null
}
```
***
#### <h4 id="6">学生相关</h4>
### /student  GET  学生权限
获取该学生（登录者）的信息   
无参数  
返回值：code=0时成功，data表示该学生的json串。    
例：例一
```
{
  "code": "0000",
  "msg": "成功",
  "data": {
    "username": "1",
    "pwd": null,
    "name": "周杰伦",
    "phone": "13998232723",
    "email": "zhoujielun@gmail.com",
    "project": {
      "id": 1111,
      "name": "",
      "info": "JAVA_WEB实训管理系统",
      "beginDate": 1491753600000,
      "phases": [],
      "students": [],
      "teacher": null
    }
  }
}
```


### /student PUT  学生权限
修改该学生的信息  
参数：必需：username,name; 可选：email, phone   
返回值：code=0时成功，data表示修改后学生的json串    
例：同例一

### /student PATCH 学生权限
修改密码  
参数：必需：oriPwd, newPwd  
返回值：code=0时成功，data表示修改后学生的json串     
例：同例一

### /students GET 管理员权限
获取所有学生列表  
无参数  
返回值：code=0时成功，data表示学生列表的json串     
例：例二
```
{
  "code": "0000",
  "msg": "成功",
  "data": [
    {
      "username": "1",
      "pwd": null,
      "name": "周杰伦",
      "phone": "13998232723",
      "email": "zhoujielun@gmail.com",
      "project": null
    },
    {
      "username": "201392001",
      "pwd": null,
      "name": "张三丰",
      "phone": "139982377845",
      "email": null,
      "project": null
    },
    ...
  ]
}
```

### /students POST 管理员权限
新增学生  
参数：必需：username,name; 可选：email, phone  
返回值：code=0时成功，data表示该学生的json串     
例：例一

### /students DELETE 管理员权限
删除学生  
参数：必需：username  
返回值：code=0时成功，data表示删除学生的json串     
例：例一
***
#### <h4 id="7">*老师相关*</h4>
### /teacher GET 老师权限
获取该老师（登录者）的信息
参数：无
返回值：code=0时成功，data为该老师的json串     
例：例三
```
{
  "code": "0000",
  "msg": "成功",
  "data": {
    "username": "1",
    "pwd": null,
    "name": "张三丰",
    "phone": "110",
    "email": "111@gmail.com",
    "info": "JAVA金牌讲师",
    "projects": [
      {
        "id": 1111,
        "name": "",
        "info": "JAVA_WEB实训管理系统",
        "beginDate": 1491753600000,
        "phases": [],
        "students": [],
        "teacher": null
      },
      ...
    ]
  }
}

```

### /teacher PUT 老师权限
修改该老师的信息
参数：必需：username,name; 可选：email, phone, info  
返回值：code=0时成功，data为该老师的json串   
例：同例三

### /teacher PATCH 老师权限
更新密码
参数：必需：oriPwd, newPwd  
返回值：code=0时成功，data表示修改后老师的json串  
例：同例三  

### /teachers GET 管理员权限
获取所有老师列表  
无参数  
返回值：code=0时成功，data表示老师列表的json串  
例：例四
```
{
  "code": "0000",
  "msg": "成功",
  "data": [
    {
      "username": "1",
      "pwd": null,
      "name": "张三丰",
      "phone": "110",
      "email": "111@gmail.com",
      "info": "JAVA金牌讲师",
      "projects": []
    },
    {
      "username": "200192011",
      "pwd": null,
      "name": "周杰伦",
      "phone": null,
      "email": null,
      "info": "东软Android讲师",
      "projects": []
    },
    ...
  ]
}
```

### /teachers POST 管理员权限
新增老师
参数：必需：username,name; 可选：email, phone, info  
返回值：code=0时成功，data表示该老师的json串  
例：例三

### /teachers DELETE 管理员权限
删除老师
参数：必需：username  
返回值：code=0时成功，data表示删除老师的json串   
例：例三
***

#### <h4 id="8">*项目相关*</h4>       

### /projects GET 老师权限
获取所有项目列表   
参数：无   
返回值:code=0时成功，data为所有项目的集合   
例：例五
```
{
  "code": "0000",
  "msg": "成功",
  "data": [
    {
      "id": 1111,
      "name": "",
      "info": "JAVA_WEB实训管理系统",
      "beginDate": 1491753600000,
      "phases": [],
      "students": [
        {
          "username": "1",
          "pwd": null,
          "name": "周杰伦",
          "phone": null,
          "email": null,
          "project": null
        },
        {
          "username": "201392001",
          "pwd": null,
          "name": "张三丰",
          "phone": null,
          "email": null,
          "project": null
        }
      ],
      "teacher": {
        "username": "1",
        "pwd": null,
        "name": "张三丰",
        "phone": null,
        "email": null,
        "info": null,
        "projects": []
      }
    },
   ...
  ]
}
```

### /projects POST 老师权限
新建项目   
参数：必需：name 可选：info， dateString  
返回值：code=0时成功，返回创建的项目(id为自动分配)
例：例六
```
{
  "code": "0000",
  "msg": "成功",
  "data": {
    "id": 1119,
    "name": "新建项目",
    "info": "嵌入式项目",
    "beginDate": 1491753600000,
    "phases": [],
    "students": [],
    "teacher": {
      "username": "1",
      "pwd": null,
      "name": "张三丰",
      "phone": null,
      "email": null,
      "info": null,
      "projects": []
    }
  }
}
```

### /projects DELETE 老师权限
删除项目  
参数：必需：id  
返回值：code=0时成功，返回删除的项目  
例：例六

### /projects/{id:\\\\d+} GET 暂未加入权限控制
{id:\\\\d+}表示整数型的路径参数，如/projects/1或/projects/2001  
获取项目详情（项目ID为路径参数id）  
参数：无  
返回值：code=0时成功，返回该项目  
例：同例六

### /projects/{id:\\\\d+} PUT 暂未加入权限控制
修改项目信息  
参数：可选：name，info，dateString  
返回值：code=0时成功，返回修改后的项目  
例：同例六

### /projects/{projectId:\\\\d+}/members GET
获取项目(项目ID为路径参数projectId)的所有成员  
参数：无  
返回值：code=0时成功，返回student对象的集合  
例：  
```
{
  "code": "0000",
  "msg": "成功",
  "data": [
    {
      "username": "1",
      "pwd": null,
      "name": "周杰伦",
      "phone": "13998232723",
      "email": "zhoujielun@gmail.com",
      "project": null
    },
    {
      "username": "201392001",
      "pwd": null,
      "name": "张三丰",
      "phone": "139982377845",
      "email": null,
      "project": null
    }
  ]
}
```

### /projects/{projectId:\\\\d+}/members POST
在该项目下新增成员    
参数：必需：username    
返回值：code=0时成功，返回新增的student对象  
例：同例一

### /projects/{projectId:\\\\d+}/members DELETE
在该项目下删除成员    
参数：必需：username    
返回值：code=0时成功，返回被删除的student对象  
例：同例一
***

#### <h4 id="9">*项目阶段相关*</h4> 
暂未加入权限控制

### /projects/{projectId:\\\\d+}/phases  GET 
获取项目（项目ID为路径参数projectId）的所有阶段列表  
参数：无  
返回值：code=0时成功，返回项目包含的所有阶段集合  
例：例七
```
{
  "code": "0000",
  "msg": "成功",
  "data": [
    {
      "projectId": 1111,
      "phaseId": 1,
      "name": "编码",
      "fileName": "18675763.png"
    },
    {
      "projectId": 1111,
      "phaseId": 2,
      "name": "编码",
      "fileName": null
    },
    {
      "projectId": 1111,
      "phaseId": 3,
      "name": "测试",
      "fileName": null
    }
  ]
}
```

### /projects/{projectId:\\\\d+}/phases  POST
在项目projectId下新建阶段  
参数：必需：phaseId，name  
返回值：code=0时成功，返回新建的项目阶段  
例：  
```
{
  "code": "0000",
  "msg": "成功",
  "data": {
    "projectId": 1111,
    "phaseId": 4,
    "name": "阶段4",
    "fileName": null
  }
}
```

### /projects/{projectId:\\\\d+}/phases  PUT
在项目projectId下修改阶段信息  
参数：必需：phaseId，name  
返回值：code=0时成功，返回修改后的项目阶段  
例：同上  

### /projects/{projectId:\\\\d+}/phases  DELETE
在项目projectId下删除阶段  
参数：必需：phaseId  
返回值：code=0时成功，返回被删除的项目阶段  
例：同上  



### /projects/{projectId}/{phaseId}/upload POST
在项目projectId的阶段phaseId下上传文件  
参数：必需：File  
返回值：code=0时成功，data为空  
例：  
```
{
  "code": "0000",
  "msg": "成功",
  "data": null
}
```

### /projects/{projectId}/{phaseId}/download GET
在项目projectId的阶段phaseId下下载文件  
参数：无  
返回值：1该阶段不存在时   
```
{"code":"2003","msg":"参数错误","data":null}
```

2该阶段下无文件时  
```
{"code":"3003","msg":"文件不存在","data":null}
```
3成功时，直接开始附件下载  





