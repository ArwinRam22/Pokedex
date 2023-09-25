package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import javax.swing.*;

public class PokeDex extends JPanel implements ActionListener, FocusListener
{
    JTextField nameField, dexField, typeField, abilityField;
    JTextField hpField, atkField, defField, spAtkField, spDefField, speField, totField;
    boolean pokemonSet = false;
    JLabel pokemonDisplay;
    int GAP = 10;

    JButton nextPokemonButton, prevPokemonButton;

    String clearButtonCode = "Clear";
    String openButtonCode = "Open";
    String nextButtonCode = "Next";
    String prevButtonCode = "Prev";
    File file;
    String currentGeneration = "";

    public PokeDex()
    {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        JPanel basicInfoPanel = new JPanel()
        {
            public Dimension getMaximumSize()
            {
                Dimension pref = getPreferredSize();
                return new Dimension(Integer.MAX_VALUE, pref.height);
            }
        };
        basicInfoPanel.setLayout(new BoxLayout(basicInfoPanel, BoxLayout.PAGE_AXIS));
        basicInfoPanel.add(createButtons());
        basicInfoPanel.add(createEntryFields());
        basicInfoPanel.add(createSwitchButtons());
        add(basicInfoPanel);

        JPanel statPanel = new JPanel()
        {
            public Dimension getMaximumSize()
            {
                Dimension pref = getPreferredSize();
                return new Dimension(Integer.MAX_VALUE, pref.height);
            }
        };
        statPanel.setLayout(new BoxLayout(statPanel, BoxLayout.PAGE_AXIS));
        statPanel.add(createStatFields());
        add(statPanel);

        JPanel pokemonDisplayPanel = new JPanel()
        {
            public Dimension getMaximumSize()
            {
                Dimension pref = getPreferredSize();
                return new Dimension(Integer.MAX_VALUE, pref.height);
            }
        };
        pokemonDisplayPanel.setLayout(new BoxLayout(pokemonDisplayPanel, BoxLayout.PAGE_AXIS));
        pokemonDisplayPanel.add(createPokemonDisplay());

        add(pokemonDisplayPanel);
        updateDisplays();
    }

    private JComponent createButtons()
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        JButton button = new JButton("Set Pokemon");
        button.addActionListener(this);
        panel.add(button);

        button = new JButton("Clear Pokemon");
        button.addActionListener(this);
        button.setActionCommand(clearButtonCode);
        panel.add(button);

        button = new JButton("Open File", createImageIcon("images/Open16.gif"));
        button.addActionListener(this);
        button.setActionCommand(openButtonCode);
        panel.add(button);

        panel.setBorder(BorderFactory.createEmptyBorder(GAP-5, GAP-5, GAP-5, GAP-5));

        return panel;
    }
    
    private JComponent createSwitchButtons()
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        nextPokemonButton = new JButton("Next Pokemon");
        nextPokemonButton.addActionListener(this);
        nextPokemonButton.setActionCommand(nextButtonCode);
        panel.add(nextPokemonButton);
        nextPokemonButton.setEnabled(false);

        prevPokemonButton = new JButton("Prev Pokemon");
        prevPokemonButton.addActionListener(this);
        prevPokemonButton.setActionCommand(prevButtonCode);
        panel.add(prevPokemonButton);
        prevPokemonButton.setEnabled(false);

        panel.setBorder(BorderFactory.createEmptyBorder(GAP-5, GAP-5, GAP-5, GAP-5));

        return panel;
    }

    private JComponent createEntryFields()
    {
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelStrings = {
            "Pokemon Name: ",
            "Pokedex #: ",
            "Typing: ",
            "Abilities: ",
        };
        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = new JComponent[labelStrings.length];
        int fieldNum = 0;

        nameField = new JTextField();
        fields[fieldNum++] = nameField;

        dexField = new JTextField();
        fields[fieldNum++] = dexField;

        typeField = new JTextField();
        fields[fieldNum++] = typeField;

        abilityField = new JTextField();
        fields[fieldNum++] = abilityField;

        for (int i = 0; i < labelStrings.length; i++)
        {
            labels[i] = new JLabel(labelStrings[i], JLabel.TRAILING);
            labels[i].setLabelFor(fields[i]);

            try 
            {
                if (i > 0)
                    ((JTextField) fields[i]).setEditable(false);
                ((JTextField) fields[i]).setColumns(20);
            } catch (ClassCastException e) {}
            
            panel.add(labels[i]);
            panel.add(fields[i]);

            JTextField tf = null;
            tf = (JTextField)fields[i];
            tf.addActionListener(this);
            tf.addFocusListener(this);
        }

        SpringUtilities.makeCompactGrid(panel,
                                        labelStrings.length, 2,
                                        GAP, GAP, //init x,y
                                        GAP, GAP/2);//xpad, ypad

        return panel;
    }

    private JComponent createStatFields()
    {
        JPanel panel = new JPanel(new SpringLayout());
        String[] labelStrings = {
            "HP: ", 
            "ATK: ", 
            "DEF: ", 
            "SP. ATK: ", 
            "SP. DEF: ", 
            "SPE: ", 
            "TOT: ", 
        };
        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = new JComponent[labelStrings.length];
        int fieldNum = 0;

        hpField = new JTextField();
        fields[fieldNum++] = hpField;

        atkField = new JTextField();
        fields[fieldNum++] = atkField;

        defField = new JTextField();
        fields[fieldNum++] = defField;

        spAtkField = new JTextField();
        fields[fieldNum++] = spAtkField;

        spDefField = new JTextField();
        fields[fieldNum++] = spDefField;

        speField = new JTextField();
        fields[fieldNum++] = speField;

        totField = new JTextField();
        fields[fieldNum++] = totField;  

        for (int i = 0; i < labelStrings.length; i++)
        {
            labels[i] = new JLabel(labelStrings[i], JLabel.TRAILING);
            labels[i].setLabelFor(fields[i]);
            try 
            {
                ((JTextField) fields[i]).setEditable(false);
                ((JTextField) fields[i]).setColumns(20);
            } catch (ClassCastException e) {}

            panel.add(labels[i]);
            panel.add(fields[i]);
        }
        SpringUtilities.makeCompactGrid(panel, 
                                        labelStrings.length, 2, 
                                        GAP, GAP, 
                                        GAP, GAP/2);

        panel.setPreferredSize(new Dimension(100, 400));
        return panel;
    }

    private JComponent createPokemonDisplay()
    {
        JPanel panel = new JPanel(new BorderLayout());
        
        pokemonDisplay = new JLabel();
        pokemonDisplay.setHorizontalAlignment(JLabel.CENTER);

        panel.setBorder(BorderFactory.createEmptyBorder(GAP/2, 0, GAP/2, 0));
        panel.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.LINE_START);
        panel.add(pokemonDisplay, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(150, 250));

        return panel;
    }

    private ImageIcon createImageIcon(String path)
    {
        java.net.URL imgURL = PokeDex.class.getResource(path);
        if (imgURL != null)
            return new ImageIcon(imgURL);
        else
        {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private void updateDisplays()
    {
        int pokemonImageWidth = 200, pokemonImageHeight = 200;
        if (pokemonSet)
        {
            String[][] pokemonList = getPokemonList();
            String[] foundPokemon = null;
            if (!nameField.getText().equals(null))
                foundPokemon = getPokemon(pokemonList);
            
            if (foundPokemon != null)
            {
                Image pokemonImage = createImageIcon("images/" + currentGeneration + "/" + foundPokemon[0].toLowerCase() + ".png").getImage().getScaledInstance(pokemonImageWidth, pokemonImageHeight, java.awt.Image.SCALE_SMOOTH);
                pokemonDisplay.setIcon(new ImageIcon(pokemonImage));

                nameField.setText(foundPokemon[0]);
                dexField.setText(foundPokemon[1]);
                typeField.setText(foundPokemon[2]);
                abilityField.setText(foundPokemon[3]);
                hpField.setText(foundPokemon[4]);
                atkField.setText(foundPokemon[5]);
                defField.setText(foundPokemon[6]);
                spAtkField.setText(foundPokemon[7]);
                spDefField.setText(foundPokemon[8]);
                speField.setText(foundPokemon[9]);

                int total = 0;
                for (int i = 4; i < foundPokemon.length; i++)
                    total += Integer.parseInt(foundPokemon[i]);
                totField.setText(String.valueOf(total));

                nextPokemonButton.setEnabled(true);
                prevPokemonButton.setEnabled(true);
                int currentIndex = findPokemon(pokemonList);
                if (currentIndex == pokemonList.length-1)
                    nextPokemonButton.setEnabled(false);
                else if (currentIndex == 0)
                    prevPokemonButton.setEnabled(false);
            }
        }
        else
        {
            Image icon = createImageIcon("images/???.png").getImage().getScaledInstance(pokemonImageWidth, pokemonImageHeight,  java.awt.Image.SCALE_SMOOTH);
            pokemonDisplay.setIcon(new ImageIcon(icon));
        }
    }

    private String[][] getPokemonList()
    {
        StringBuffer pokemonStringBuffer = new StringBuffer();
        String[] arr;
        String text = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while ((text = reader.readLine()) != null)
                pokemonStringBuffer.append(text + "@");
        }
        catch(IOException e){
            System.err.println("Error reading file.");
        }
        arr = pokemonStringBuffer.toString().split("@");
        String[][] pokemonList = new String[arr.length][10];
        for (int i = 0; i < arr.length; i++)
            pokemonList[i] = arr[i].split("#");
        
        return pokemonList;
    }

    private String[] getPokemon(String[][] pokemonList)
    {
        String find = nameField.getText();
        int index = 0;
        while (index < pokemonList.length && !pokemonList[index][0].equals(find.toUpperCase()))
            index++;
        if (index == pokemonList.length) return null;
        return pokemonList[index];
    }

    private int findPokemon(String[][] pokemonList)
    {
        String find = nameField.getText();
        int index = 0;
        while (index < pokemonList.length && !pokemonList[index][0].equals(find.toUpperCase()))
            index++;
        return index;
    }
    
    public void focusGained(FocusEvent e)
    {
        Component c = e.getComponent();
        if (c instanceof JFormattedTextField) {
            selecItLater(c);
        }
        else if (c instanceof JTextField) {
            ((JTextField)c).selectAll();
        }
    }
    
    private void selecItLater(Component c)
    {
        final JFormattedTextField ftf = (JFormattedTextField)c;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ftf.selectAll();
            }
        });
    }

    public void focusLost(FocusEvent e) {}

    public void actionPerformed(ActionEvent e) 
    {
        if (clearButtonCode.equals(e.getActionCommand()))
        {
            pokemonSet = false;
            nameField.setText("");
            dexField.setText("");
            typeField.setText("");
            abilityField.setText("");

            hpField.setText("");
            atkField.setText("");
            defField.setText("");
            spAtkField.setText("");
            spDefField.setText("");
            speField.setText("");
            totField.setText("");

            nextPokemonButton.setEnabled(false);
            prevPokemonButton.setEnabled(false);
        }
        else if (openButtonCode.equals(e.getActionCommand()))
        {
            JFileChooser fc = new JFileChooser(Paths.get("").toAbsolutePath().toString());
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                currentGeneration = file.getName().substring(0, 5);
            }
        }
        else
        {
            String[][] pokemonList = getPokemonList();
            int currentIndex = findPokemon(pokemonList);
            if (nextButtonCode.equals(e.getActionCommand()))
                nameField.setText(pokemonList[currentIndex+1][0]);
            else if (prevButtonCode.equals(e.getActionCommand()))
                nameField.setText(pokemonList[currentIndex-1][0]);
            pokemonSet = true;
        }
        
        updateDisplays();
    }
}