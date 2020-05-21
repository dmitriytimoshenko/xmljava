

import org.postgresql.ds.PGSimpleDataSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class App {
    public static void main(String[] args) throws Exception{

            PGSimpleDataSource ds = new PGSimpleDataSource();

            FileInputStream fis = new FileInputStream("src/main/resources/connection.properties");
            Properties properties = new Properties();
            properties.load(fis);


            System.out.println(properties);


            String url = (String) properties.get("jdbcUrl");
            String username = (String) properties.get("username");
            String password = (String) properties.get("password");


            System.out.println(url);
            System.out.println(username);
            System.out.println(password);


            ds.setUrl(url);
            ds.setUser(username);
            ds.setPassword(password);

            Connection connection = ds.getConnection();
            Statement statement = connection.createStatement();


            ResultSet rs = statement.executeQuery("select * from text limit 5");


            //-------Собираем XML из результ сета

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element el1 = document.createElement("TextTable");
            document.appendChild(el1);


            System.out.println("Table TEXT:");
            while (rs.next()) {
                System.out.println(rs.getString("id_col") + "  " + rs.getString("string"));
                Element el2 = document.createElement("Strings");

                el2.setAttribute("id", rs.getString(1));
                el2.setAttribute("text", rs.getString(2));
                el1.appendChild(el2);
            }

            DOMSource domSource = new DOMSource(document);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(domSource, new StreamResult(new File("src/main/resources/AttributeXML.xml")));

            //------- Преобразуем XML через XSLT шаблон

            Source xslt = new StreamSource(new File("src/main/resources/template.xsl"));
            Transformer xsltTrans = TransformerFactory.newInstance().newTransformer(xslt);
            Source xml = new StreamSource(new File("src/main/resources/AttributeXML.xml"));
            xsltTrans.transform(xml, new StreamResult(new File("src/main/resources/xsltresult.xml")));

            // -------- Теперь преобразуем новый XML в CSV
            Source xslt1 = new StreamSource(new File("src/main/resources/templateCSV.xsl"));
            Transformer xsltTrans1 = TransformerFactory.newInstance().newTransformer(xslt1);
            Source xml1 = new StreamSource(new File("src/main/resources/xsltresult.xml"));
            xsltTrans1.transform(xml1, new StreamResult(new File("src/main/resources/csvresult.csv")));


    }
}
