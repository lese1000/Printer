如何使用jackson美化输出json/xml
1.美化POJO序列化xml
下面将POJO列化为xml并打印。

Person person = new Person();
//设置person属性

ObjectMapper mapper = new XmlMapper();
System.out.println(mapper.writeValueAsString(person));
但是输出为紧凑模式：

<Person><name>Hello world</name><age>12</age></Person>
2.目的：美化过的输出
有时希望能够美化输出，更方便阅读和理解，如：

<Person>
  <name>Hello world</name>
  <age>12</age>
</Person>

方式1.使用：writerWithDefaultPrettyPrinter

ObjectMapper mapper = new XmlMapper();
System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(person));
 mapper.enable(SerializationFeature.INDENT_OUTPUT);
 
方式2.使用：SerializationFeature.INDENT_OUTPUT

ObjectMapper mapper = new XmlMapper();
mapper.enable(SerializationFeature.INDENT_OUTPUT);
mapper.writeValueAsString(person);

3.序列化为json
序列化为json时，操作基本一致，只需要使用ObjectMapper替代XmlMapper。如：

Person person = new Person();
//设置person属性

ObjectMapper mapper = new ObjectMapper();
System.out.println(mapper.writeValueAsString(person));

=>激活美化的方式，同样可以是2.1和2.2介绍的方式。

4.包依赖
序列化为xml依赖：
jackson-databind
jackson-core
jackson-dataformat-xml

序列化为json依赖:
jackson-databind
jackson-core
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
    <version>2.8.2</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.8.2</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-core</artifactId>
    <version>2.8.2</version>
</dependency>