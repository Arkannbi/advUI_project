package codeGenerator;

import blocks.Block;
import blocks.BlockType;
import blocks.Port;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import ui.Canvas;
import ui.MainFrame;

public class CodeSerializer {

    public void serializeToXML(String filePath, List<Map<String, String>> variables, MainFrame mainFrame) {
        try {
            // Create XML document
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Root element
            Element root = doc.createElement("Program");
            doc.appendChild(root);

            // --- VARIABLES SECTION ---
            Element varsElement = doc.createElement("Variables");
            root.appendChild(varsElement);

            if (variables != null) {
                for (Map<String, String> var : variables) {
                    Element varElem = doc.createElement("Variable");
                    varElem.setAttribute("name", var.get("name"));
                    varElem.setAttribute("type", var.get("type"));
                    varElem.setAttribute("value", var.get("value"));
                    varsElement.appendChild(varElem);
                }
            }

            // --- BLOCKS SECTION ---
            Element blocksElem = doc.createElement("Blocks");
            root.appendChild(blocksElem);

            List<Block> allBlocks = mainFrame.getCanvasBlocks();
            for (Block block : allBlocks) {
                Element blockElem = doc.createElement("Block");
                blockElem.setAttribute("id", String.valueOf(block.hashCode()));
                blockElem.setAttribute("type", block.getType().toString());
                blockElem.setAttribute("name", escapeXML(block.getTitle()));
                blockElem.setAttribute("x", "" + block.getX());
                blockElem.setAttribute("y", "" + block.getY());

                // Inputs
                Element inputsElem = doc.createElement("Inputs");
                blockElem.appendChild(inputsElem);
                block.getInputs().forEach(port -> {
                    Element inputElem = doc.createElement("Input");
                    inputElem.setAttribute("id", String.valueOf(port.hashCode()));
                    inputElem.setAttribute("name", String.valueOf(port.getName()));
                    if (port.isConnected()) {
                        inputElem.setAttribute("connectedTo", String.valueOf(port.getConnectedPort().hashCode()));
                    } else {
                        inputElem.setAttribute("value", String.valueOf(port.getDefaultValue()));
                    }
                    inputsElem.appendChild(inputElem);
                });

                // Outputs
                Element outputsElem = doc.createElement("Outputs");
                blockElem.appendChild(outputsElem);
                block.getOutputs().forEach(port -> {
                    Element outputElem = doc.createElement("Output");
                    outputElem.setAttribute("id", String.valueOf(port.hashCode()));
                    outputElem.setAttribute("name", String.valueOf(port.getName()));
                    if (port.isConnected())
                        outputElem.setAttribute("connectedTo", String.valueOf(port.getConnectedPort().hashCode()));
                    outputsElem.appendChild(outputElem);
                });

                blocksElem.appendChild(blockElem);
            }

            // --- Write XML file ---
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileWriter(filePath));
            transformer.transform(source, result);

            mainFrame.showLog("Serialized block structure to: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
            mainFrame.showLog("Error during serialization: " + e.getMessage());
        }
    }

    // Utility to escape code safely for XML
    private String escapeXML(String code) {
        return code
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;");
    }


    public void loadFromXML(String filePath, Canvas canvas) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Map<String, Port> portMap = new HashMap<>();

            NodeList blockNodes = doc.getElementsByTagName("Block");

            // Creation of blocks and ports
            for (int i = 0; i < blockNodes.getLength(); i++) {
                Element blockElem = (Element) blockNodes.item(i);

                String id = blockElem.getAttribute("id");
                String name = blockElem.getAttribute("name");
                String typeStr = blockElem.getAttribute("type");
                int x = Integer.parseInt(blockElem.getAttribute("x"));
                int y = Integer.parseInt(blockElem.getAttribute("y"));

                BlockType type = BlockType.valueOf(typeStr);

                List<String> inputs = new ArrayList<>();
                List<String> outputs = new ArrayList<>();
                List<String> inputDefaultValues = new ArrayList<>();
                List<String> outputDefaultValues = new ArrayList<>();

                NodeList inputNodes = ((Element) blockElem.getElementsByTagName("Inputs").item(0))
                        .getElementsByTagName("Input");

                for (int j = 0; j < inputNodes.getLength(); j++) {
                    Element input = (Element) inputNodes.item(j);
                    inputs.add(input.getAttribute("name"));
                    String defaultValue = input.hasAttribute("value") ? input.getAttribute("value") : "";
                    inputDefaultValues.add(defaultValue);
                }

                NodeList outputNodes = ((Element) blockElem.getElementsByTagName("Outputs").item(0))
                        .getElementsByTagName("Output");

                for (int j = 0; j < outputNodes.getLength(); j++) {
                    Element output = (Element) outputNodes.item(j);
                    outputs.add(output.getAttribute("name"));
                    String defaultValue = output.hasAttribute("value") ? output.getAttribute("value") : "";
                    outputDefaultValues.add(defaultValue);
                }

                Block block = new Block(name, type, inputs, outputs);

                // Ajout des valeurs par défaut
                for (int j = 0; j < block.getInputs().size(); j++) {
                    Port p = block.getInputs().get(j);
                    p.setDefaultValue(inputDefaultValues.get(j));

                    // Stocker le port par son ID XML (si disponible)
                    Element input = (Element) inputNodes.item(j);
                    String portId = input.getAttribute("id");
                    if (portId != null && !portId.isEmpty()) {
                        portMap.put(portId, p);
                    }
                }

                for (int j = 0; j < block.getOutputs().size(); j++) {
                    Port p = block.getOutputs().get(j);
                    p.setDefaultValue(outputDefaultValues.get(j));

                    Element output = (Element) outputNodes.item(j);
                    String portId = output.getAttribute("id");
                    if (portId != null && !portId.isEmpty()) {
                        portMap.put(portId, p);
                    }
                }

                canvas.addBlock(block, x, y);
            }

            // Connect the ports
            for (int i = 0; i < blockNodes.getLength(); i++) {
                Element blockElem = (Element) blockNodes.item(i);
                NodeList outputNodes = ((Element) blockElem.getElementsByTagName("Outputs").item(0))
                        .getElementsByTagName("Output");

                for (int j = 0; j < outputNodes.getLength(); j++) {
                    Element output = (Element) outputNodes.item(j);
                    String fromPortId = output.getAttribute("id");
                    String toPortId = output.getAttribute("connectedTo");

                    if (toPortId != null && !toPortId.isEmpty()) {
                        Port fromPort = portMap.get(fromPortId);
                        Port toPort = portMap.get(toPortId);

                        if (fromPort != null && toPort != null) {
                            System.out.println("From " + fromPort.getName() + " to " + toPort.getName());
                            canvas.connectPort(fromPort, toPort);
                        } else {
                            System.err.println("Cannot create connexion (" + fromPortId + " -> " + toPortId + ")");
                        }
                    }
                }
            }

            canvas.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
