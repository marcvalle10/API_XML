import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Main {

    public static void main(String[] args) {
        try {
            // Crea un DocumentBuilder
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // Parsea el archivo sales.xml
            Document doc = dBuilder.parse(new File("C:/Users/marco/IdeaProjects/API XML/src/sales.xml"));

            // Solicita al usuario el departamento
            Scanner scanner = new Scanner(System.in);
            System.out.print("Ingrese el departamento al que desea aplicar el incremento: ");
            String departamento = scanner.nextLine();

            // Solicita al usuario el porcentaje de incremento
            double incrementPercentage = solicitarPorcentaje();

            // Obtén la lista de elementos sale_record
            NodeList saleRecords = doc.getElementsByTagName("sale_record");

            // Itera a través de los elementos sale_record
            for (int i = 0; i < saleRecords.getLength(); i++) {
                Element saleRecord = (Element) saleRecords.item(i);

                // Obtén el elemento department y su valor
                Element departmentElement = (Element) saleRecord.getElementsByTagName("department").item(0);
                String currentDepartment = departmentElement.getTextContent();

                // Comprueba si el departamento coincide con el especificado por el usuario
                if (currentDepartment.equalsIgnoreCase(departamento)) {
                    // Obtén el elemento sales y su valor como double
                    Element salesElement = (Element) saleRecord.getElementsByTagName("sales").item(0);
                    double currentSales = Double.parseDouble(salesElement.getTextContent());

                    // Calcula el nuevo valor de ventas con el incremento
                    double newSales = currentSales * (1 + (incrementPercentage / 100));

                    // Actualiza el valor de ventas en el elemento sales
                    salesElement.setTextContent(String.format("%.2f", newSales));
                }
            }

            // Guarda el nuevo documento en new_sales.xml
            guardarDocumento(doc, "new_sales.xml");

            System.out.println("Proceso completado. Se ha creado el archivo new_sales.xml con los incrementos aplicados.");
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double solicitarPorcentaje() {
        double porcentaje = 0;
        Scanner scanner = new Scanner(System.in);
        while (porcentaje < 5 || porcentaje > 15) {
            System.out.print("Por favor, ingrese un porcentaje entre 5% y 15%: ");
            try {
                porcentaje = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Intente nuevamente.");
            }
        }
        return porcentaje;
    }

    private static void guardarDocumento(Document doc, String nombreArchivo) throws Exception {
        // Crea un objeto TransformerFactory
        javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();

        // Crea un objeto Transformer para escribir el documento modificado
        javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();

        // Escribe el documento modificado en el archivo
        javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(doc);
        javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(new FileOutputStream(nombreArchivo));
        transformer.transform(source, result);
    }
}
