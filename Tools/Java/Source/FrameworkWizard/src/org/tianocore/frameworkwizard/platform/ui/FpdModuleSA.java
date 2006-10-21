package org.tianocore.frameworkwizard.platform.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JSplitPane;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.tianocore.frameworkwizard.common.DataValidation;
import org.tianocore.frameworkwizard.common.GlobalData;
import org.tianocore.frameworkwizard.common.IDefaultTableModel;
import org.tianocore.frameworkwizard.common.Identifications.OpeningPlatformType;
import org.tianocore.frameworkwizard.platform.ui.global.LibraryClassDescriptor;
import org.tianocore.frameworkwizard.platform.ui.global.WorkspaceProfile;
import org.tianocore.frameworkwizard.platform.ui.global.SurfaceAreaQuery;
import org.tianocore.frameworkwizard.module.Identifications.ModuleIdentification;
import org.tianocore.frameworkwizard.packaging.PackageIdentification;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.JComboBox;

public class FpdModuleSA extends JDialog implements ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    static JFrame frame;
    private JPanel jContentPane = null;
    private JTabbedPane jTabbedPane = null;
    private JPanel jPanelPcd = null;
    private JPanel jPanelLibrary = null;
    private JLabel jLabelPcdData = null;
    private JScrollPane jScrollPaneTablePcd = null;
    private JTable jTablePcd = null;
    private JPanel jPanelPcdSouth = null;
    private JScrollPane jScrollPanePcdHelp = null;
    private JTextArea jTextAreaPcdHelp = null;
    private JPanel jPanelContentPaneSouth = null;
    private JSplitPane jSplitPane = null;
    private JPanel jPanelLibraryLeft = null;
    private JPanel jPanelLibraryRight = null;
    private JLabel jLabelLibClass = null;
    private JLabel jLabelQualifiedInstance = null;
    private JScrollPane jScrollPaneSelectedInstances = null;
    private JTable jTableSelectedInstances = null;
    private JScrollPane jScrollPaneLibClass = null;
    private JTable jTableLibClass = null;
    private JScrollPane jScrollPaneQualifiedInstance = null;
    private JTable jTableLibInstances = null;
    private JPanel jPanelLibrarySouth = null;
    private JPanel jPanelLibraryCenter = null;
    private JScrollPane jScrollPaneInstanceHelp = null;
    private JTextArea jTextAreaInstanceHelp = null;
    private JLabel jLabelSelectedInstances = null;
    private JLabel jLabelInstanceHelp = null;
    private JButton jButtonAdd = null;
    private JButton jButtonDeleteInstance = null;
    private JLabel jLabelPcdHelp = null;
    private JButton jButtonOk = null;
    private JButton jButtonCancel = null;
    private IDefaultTableModel model = null;
    private IDefaultTableModel selectedInstancesTableModel = null;
    private IDefaultTableModel libClassTableModel = null;
    private IDefaultTableModel libInstanceTableModel = null;
    private DefaultTableModel optionsTableModel = null;
    private FpdFileContents ffc = null;
    private String moduleKey = null;
    private int moduleSaNum = -1;
    private HashMap<LibraryClassDescriptor, ArrayList<String>> classInstanceMap = null;
    //
    // map of <{libName, supArch, supMod}, list of Module information>
    //
    private HashMap<LibraryClassDescriptor, ArrayList<String>> classConsumed = null;
    private HashMap<LibraryClassDescriptor, ArrayList<String>> classProduced = null;
    
    private JPanel jPanelModuleSaOpts = null;
    private JLabel jLabelFvBinding = null;
    private JTextField jTextFieldFvBinding = null;
    private JLabel jLabelFfsFileGuid = null;
    private JTextField jTextFieldFileGuid = null;
    private JLabel jLabelFfsFormatKey = null;
    private JTextField jTextFieldFfsKey = null;
    private JScrollPane jScrollPaneModuleSaOptions = null;
    private JTable jTableModuleSaOptions = null;
    private JButton jButtonNew = null;
    private JButton jButtonDeleteOption = null;
    private JPanel jPanelPcdFields = null;
    private JPanel jPanelPcdFieldsSecondRow = null;
    private JPanel jPanelPcdFieldsThirdRow = null;
    private JPanel jPanelPcdFieldsFirstRow = null;
    private JLabel jLabelItemType = null;
    private JComboBox jComboBoxItemType = null;
    private JLabel jLabelMaxDatumSize = null;
    private JTextField jTextFieldMaxDatumSize = null;
    private JLabel jLabelPcdDefaultValue = null;
    private JTextField jTextFieldPcdDefault = null;
    private JButton jButtonUpdatePcd = null;
    private JComboBox jComboBoxFeatureFlagValue = null;
    private OpeningPlatformType docConsole = null;
    private JPanel jPanelCustomToolChain = null;
    private JPanel jPanelToolchainS = null;
    private JPanel jPanelLibraryCenterN = null;
    private JPanel jPanelLibraryCenterC = null;  //  @jve:decl-index=0:visual-constraint="20,224"
    
    private final int buildTargetWidth = 150;
    private final int toolChainFamilyWidth = 150;
    private final int supportArchWidth = 150;
    private final int toolCmdCodeWidth = 200;
    private final int tagNameWidth = 150;
    private final int argWidth = 400;
    
    /**
     * This is the default constructor
     */
    public FpdModuleSA() {
        super();
        initialize();
    }
    public FpdModuleSA(FpdFileContents ffc) {
        this();
        this.ffc = ffc;
    }
    
    public void setKey(String k, int i, OpeningPlatformType dc){
        this.moduleKey = k;
        moduleSaNum = i;
        this.docConsole = dc;
        classInstanceMap = null;
        classProduced = null;
        classConsumed = null;
        jTabbedPane.setSelectedIndex(0);
        initPcdBuildDefinition(i);
        ModuleIdentification mi = WorkspaceProfile.getModuleId(moduleKey);
        int tabIndex = jTabbedPane.indexOfTab("Libraries");
        if (mi.isLibrary()) {
            jTabbedPane.setEnabledAt(tabIndex, false);
        }
        else {
            jTabbedPane.setEnabledAt(tabIndex, true);
        }
    }

    /**
      init will be called each time FpdModuleSA object is to be shown.
      @param key Module information.
     **/
    public void initPcdBuildDefinition(int i) {
        //
        // display pcd for key.
        //
        model.setRowCount(0);
        jTextAreaPcdHelp.setText("");
        jComboBoxItemType.setSelectedIndex(-1);
        jTextFieldMaxDatumSize.setText("");
        jTextFieldPcdDefault.setText("");
        int pcdCount = ffc.getPcdDataCount(i);
        if (pcdCount != 0) {
            String[][] saa = new String[pcdCount][7];
            ffc.getPcdData(i, saa);
            for (int j = 0; j < saa.length; ++j) {
                model.addRow(saa[j]);
            }
        }
    }
    
    public void initLibraries(String key) {
        try {
            //
            // display library classes that need to be resolved. also potential instances for them.
            //
            resolveLibraryInstances(moduleKey);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e.getMessage());
        }
        //
        // display lib instances already selected for key
        //
        selectedInstancesTableModel.setRowCount(0);
        int instanceCount = ffc.getLibraryInstancesCount(key);
        if (instanceCount != 0) {
            String[][] saa = new String[instanceCount][5];
            ffc.getLibraryInstances(key, saa);
            for (int i = 0; i < saa.length; ++i) {
                ModuleIdentification mi = WorkspaceProfile.getModuleId(saa[i][1] + " " + saa[i][2] + " " + saa[i][3]
                                                                       + " " + saa[i][4]);
                if (mi != null) {
                    //
                    // ToDo: verify this instance first.
                    //
                    saa[i][0] = mi.getName();
                    saa[i][2] = mi.getVersion();
                    saa[i][4] = mi.getPackageId().getVersion();
                    //
                    // re-evaluate lib instance usage when adding a already-selected lib instance.
                    //
                    try {
                        resolveLibraryInstances(saa[i][1] + " " + saa[i][2] + " " + saa[i][3] + " " + saa[i][4]);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, e.getMessage());
                    }
                    selectedInstancesTableModel.addRow(saa[i]);
                }
            }
        }

        showClassToResolved();
    }
    
    public void initFvInfo (String key) {
        //
        // display module SA options
        //
        jTextFieldFvBinding.setText("");
        String fvBinding = ffc.getFvBinding(key);
        if (fvBinding != null) {
            jTextFieldFvBinding.setText(fvBinding);
        }
        jTextFieldFileGuid.setText("");
        String fileGuid = ffc.getFfsFileNameGuid(key);
        if (fileGuid != null) {
            jTextFieldFileGuid.setText(fileGuid);
        }
        jTextFieldFfsKey.setText("");
        String ffsKey = ffc.getFfsFormatKey(key);
        if (ffsKey != null) {
            jTextFieldFfsKey.setText(ffsKey);
        }
    }
    
    public void initToolChainOptions(String key) {
        
        optionsTableModel.setRowCount(0);
        String[][] saa = new String[ffc.getModuleSAOptionsCount(key)][6];
        ffc.getModuleSAOptions(key, saa);
        for (int i = 0; i < saa.length; ++i) {
            optionsTableModel.addRow(saa[i]);
        }
    }
    
    private void filterClassConsumedByArch (Vector<LibraryClassDescriptor> v) {
        String[] moduleInfo = moduleKey.split(" ");
        Vector<String> vModuleArchs = new Vector<String>();
        //
        // Skip guid, version information, get archs to check.
        //
        for (int i = 4; i < moduleInfo.length; ++i) {
            vModuleArchs.add(moduleInfo[i]);
        }
        //
        // if module will be built on all platforms, no filter needed for lib classes.
        //
        if (vModuleArchs.size() == 0) {
            return;
        }
        
        for (int j = 0; j < v.size(); ++j) {
            LibraryClassDescriptor libInfo = v.get(j);

            Vector<String> vSupArchs = libInfo.getVectorFromString(libInfo.supArchs);
            
            if (vSupArchs.size() == 0 || (vSupArchs.size() == 1 && vSupArchs.get(0).equalsIgnoreCase(""))) {
                //
                // update lib info to module archs only.
                //
                libInfo.supArchs = "";
                for (int i = 0; i < vModuleArchs.size(); ++i) {
                    libInfo.supArchs += vModuleArchs.get(i);
                    libInfo.supArchs += " ";
                }
                libInfo.supArchs.trim();
                continue;
            }
            //
            // only retain those lib class used by module archs.
            //
            vSupArchs.retainAll(vModuleArchs);
            if (vSupArchs.size() > 0) {
                //
                // update lib info to reflect which kind of arch need to select instance.
                //
                libInfo.supArchs = "";
                for (int i = 0; i < vSupArchs.size(); ++i) {
                    libInfo.supArchs += vSupArchs.get(i);
                    libInfo.supArchs += " ";
                }
                libInfo.supArchs.trim();
                continue;
            }
            //
            // remove this lib definition if it supports no archs module will be built under.
            //
            v.iterator().remove();
        }
    }
    
    private void resolveLibraryInstances(String key) throws MultipleInstanceException, NoInstanceException{
        ModuleIdentification mi = WorkspaceProfile.getModuleId(key);
        PackageIdentification[] depPkgList = null;
        
        //
        // Get dependency pkg list into which we will search lib instances.
        //
        depPkgList = SurfaceAreaQuery.getDependencePkg(null, mi);
        //
        // Get the lib class consumed, produced by this module itself.
        //
        Vector<LibraryClassDescriptor> vClassConsumed = SurfaceAreaQuery.getLibraryClasses("ALWAYS_CONSUMED", mi);
        filterClassConsumedByArch(vClassConsumed);
        if (this.classConsumed == null) {
            this.classConsumed = new HashMap<LibraryClassDescriptor, ArrayList<String>>();
        }

        for (int i = 0; i < vClassConsumed.size(); ++i) {
            ArrayList<String> consumedBy = this.classConsumed.get(vClassConsumed.get(i));
            if (consumedBy == null) {
                consumedBy = new ArrayList<String>();
            }
            consumedBy.add(key);
            this.classConsumed.put(vClassConsumed.get(i), consumedBy);
        }

        Vector<LibraryClassDescriptor> vClassProduced = SurfaceAreaQuery.getLibraryClasses("ALWAYS_PRODUCED", mi);
        if (this.classProduced == null) {
            this.classProduced = new HashMap<LibraryClassDescriptor, ArrayList<String>>();
        }
        for (int i = 0; i < vClassProduced.size(); ++i) {
            ArrayList<String> producedBy = this.classProduced.get(vClassProduced.get(i));
            if (producedBy == null) {
                producedBy = new ArrayList<String>();
            }
            //
            // class already produced by previous module (lib instance).
            /*
            if (producedBy.size() == 1) {
                String instanceKey = producedBy.get(0);
                ModuleIdentification libMi = WorkspaceProfile.getModuleId(instanceKey);
                throw new MultipleInstanceException (vClassProduced.get(i).className, libMi.getName(), mi.getName());
            }
            Iterator<LibraryClassDescriptor> lcdi = this.classProduced.keySet().iterator();
            while (lcdi.hasNext()) {
                LibraryClassDescriptor lcd = lcdi.next();
                if (vClassProduced.get(i).hasInterSectionWith(lcd)) {
                    ArrayList<String> alreadyProducedBy = this.classProduced.get(lcd);
                    String instanceKey = alreadyProducedBy.get(0);
                    ModuleIdentification libMi = WorkspaceProfile.getModuleId(instanceKey);
                    throw new MultipleInstanceException (vClassProduced.get(i).className, libMi.getName(), mi.getName());
                }
            }
            */
            // normal case.
            //
            producedBy.add(key);
            this.classProduced.put(vClassProduced.get(i), producedBy);
            
        }

        //
        // find potential instances in all pkgs for classes still in classConsumed.
        //
        if (classInstanceMap == null) {
            classInstanceMap = new HashMap<LibraryClassDescriptor, ArrayList<String>>();
        }
        Iterator<LibraryClassDescriptor> lic = this.classConsumed.keySet().iterator();
        while (lic.hasNext()) {
            LibraryClassDescriptor cls = lic.next();
            if (isBoundedClass(cls)) {
                continue;
            }
            ArrayList<String> instances = getInstancesForClass(cls, depPkgList);
            if (instances.size() == 0) {
                throw new NoInstanceException (cls.className);
            }
            classInstanceMap.put(cls, instances);

        }
//            showClassToResolved();
    }

    /**Search classProduced map to see if this class has been produced by some instance (module).
     * @param cls
     * @return
     */
    private boolean isBoundedClass (LibraryClassDescriptor cls) {
        if (this.classProduced.containsKey(cls)) {
            return true;
        }
        Iterator<LibraryClassDescriptor> lcdi = this.classProduced.keySet().iterator();
        while (lcdi.hasNext()) {
            LibraryClassDescriptor lcd = lcdi.next();
            if (cls.isSubSetByArchs(lcd) && cls.isSubSetByModTypes(lcd)) {
                return true;
            }
        }
        
        return false;
    }
    
    private ArrayList<String> getInstancesForClass(LibraryClassDescriptor cls, PackageIdentification[] depPkgList){
        ArrayList<String> al = new ArrayList<String>();
        
//        for (int i = 0; i < depPkgList.length; ++i) {
            Iterator ismi = GlobalData.vModuleList.iterator();
            while(ismi.hasNext()) {
                ModuleIdentification mi = (ModuleIdentification)ismi.next();
//                if (!mi.getPackageId().getGuid().equalsIgnoreCase(depPkgList[i].getGuid())) {
//                    continue;
//                }
                Vector<LibraryClassDescriptor> clsProduced = SurfaceAreaQuery.getLibraryClasses("ALWAYS_PRODUCED", mi);
                
                boolean isPotential = false;
                Iterator<LibraryClassDescriptor> lcdi = clsProduced.iterator();
                while (lcdi.hasNext()) {
                    LibraryClassDescriptor lcd = lcdi.next();
                    if (cls.isSubSetByArchs(lcd) && cls.isSubSetByModTypes(lcd)){
                        isPotential = true;
                    }
                    
                    if (hasBeenProduced(lcd)) {
                        isPotential = false;
                        break;
                    }
                }
                if (isPotential) {
                    al.add(mi.getGuid() + " " + mi.getVersion() + " " + 
                           mi.getPackageId().getGuid() + " " + mi.getPackageId().getVersion());
                }
            }
//        }
        
        return al;
    }
    
    private boolean hasBeenProduced (LibraryClassDescriptor cls) {
        Iterator<LibraryClassDescriptor> lcdi = this.classProduced.keySet().iterator();
        while (lcdi.hasNext()) {
            LibraryClassDescriptor lcd = lcdi.next();
            if (cls.hasInterSectionWith(lcd)) {
                return true;
            }
        }
        return false;
    }
    
    private ArrayList<String> getConsumedBy (String className) {
        Iterator<LibraryClassDescriptor> lcdi = this.classConsumed.keySet().iterator();
        while (lcdi.hasNext()) {
            LibraryClassDescriptor lcd = lcdi.next();
            if (lcd.className.equals(className)) {
                return this.classConsumed.get(lcd);
            }
        }
        return null;
    }
    
    private void removeInstance(String key) {
        ModuleIdentification mi = WorkspaceProfile.getModuleId(key); 
        //
        // remove pcd information of instance from current ModuleSA
        //
        ffc.removePcdData(moduleKey, mi);
        //
        // remove class produced by this instance and add back these produced class to be bound.
        //
        Vector<LibraryClassDescriptor> clsProduced = getClassProduced(mi);
        for (int i = 0; i < clsProduced.size(); ++i) {
            
            classProduced.remove(clsProduced.get(i));
        }
        //
        // remove class consumed by this instance. we do not need to bound it now.
        //
        String[] clsConsumed = getClassConsumed(mi);
        for (int i = 0; i < clsConsumed.length; ++i) {
            ArrayList<String> al = getConsumedBy (clsConsumed[i]);
            
            if (al == null ) {
                continue;
            }
            al.remove(key);
            
        }
        
        showClassToResolved();
        
    }
    
    
    private Vector<LibraryClassDescriptor> getClassProduced(ModuleIdentification mi){
        
        Vector<LibraryClassDescriptor> clsProduced = SurfaceAreaQuery.getLibraryClasses("ALWAYS_PRODUCED", mi);
        return clsProduced;
//        String[] sClassProduced = new String[clsProduced.size()];
//        for (int i = 0; i < clsProduced.size(); ++i) {
//            sClassProduced[i] = clsProduced.get(i).className;
//        }
//        return sClassProduced;
    }
    
    private String[] getClassConsumed(ModuleIdentification mi){
        
        Vector<LibraryClassDescriptor> clsConsumed = SurfaceAreaQuery.getLibraryClasses("ALWAYS_CONSUMED", mi);
        String[] sClassConsumed = new String[clsConsumed.size()];
        for (int i = 0; i < clsConsumed.size(); ++i) {
            sClassConsumed[i] = clsConsumed.get(i).className;
        }
        return sClassConsumed;
    }
    
    private void showClassToResolved(){
        libClassTableModel.setRowCount(0);
        if (classConsumed == null || classConsumed.size() == 0) {
            return;
        }
        Iterator<LibraryClassDescriptor> li = classConsumed.keySet().iterator();
        while(li.hasNext()){
            LibraryClassDescriptor lcd = li.next();
            String[] s = {lcd.className, lcd.supArchs, lcd.supModTypes};
            if (classConsumed.get(lcd) == null || classConsumed.get(lcd).size() == 0) {
                continue;
            }
            
            if (!isBoundedClass(lcd)){
                libClassTableModel.addRow(s);
            }
        }
        libInstanceTableModel.setRowCount(0);
    }
    
    private void addLibInstance (ModuleIdentification libMi) throws Exception{
        
        //
        // Add pcd information of selected instance to current moduleSA
        //
        ffc.addFrameworkModulesPcdBuildDefs(libMi, null, ffc.getModuleSA(moduleKey));
        
        ffc.genLibraryInstance(libMi, moduleKey);
    }
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setSize(877, 555);
        this.setResizable(false);
        this.centerWindow();
        this.setModal(true);
        this.setTitle("Module Settings");
        this.setContentPane(getJContentPane());
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJTabbedPane(), java.awt.BorderLayout.CENTER);
            jContentPane.add(getJPanelContentPaneSouth(), java.awt.BorderLayout.SOUTH);
        }
        return jContentPane;
    }

    /**
     * This method initializes jTabbedPane	
     * 	
     * @return javax.swing.JTabbedPane	
     */
    private JTabbedPane getJTabbedPane() {
        if (jTabbedPane == null) {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.addTab("PCD Build Definition", null, getJPanelPcd(), null);
            jTabbedPane.addTab("Libraries", null, getJPanelLibrary(), null);
            jTabbedPane.addTab("FV Info", null, getJPanelModuleSaOpts(), null);
            jTabbedPane.addTab("Custom Toolchain", null, getJPanelCustomToolChain(), null);
            
        }
        return jTabbedPane;
    }

    /**
     * This method initializes jPanelPcd
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelPcd() {
        if (jPanelPcd == null) {
            jLabelPcdData = new JLabel();
            jLabelPcdData.setText(" PCD Data");
            jPanelPcd = new JPanel();
            jPanelPcd.setLayout(new BorderLayout());
            jPanelPcd.add(jLabelPcdData, java.awt.BorderLayout.NORTH);
            jPanelPcd.add(getJScrollPaneTablePcd(), java.awt.BorderLayout.CENTER);
            jPanelPcd.add(getJPanelPcdSouth(), java.awt.BorderLayout.SOUTH);
            jPanelPcd.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentShown(java.awt.event.ComponentEvent e) {
                    initPcdBuildDefinition(moduleSaNum);
                }
            });
            
        }
        return jPanelPcd;
    }

    /**
     * This method initializes jPanelLibrary
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelLibrary() {
        if (jPanelLibrary == null) {
            jPanelLibrary = new JPanel();
            jPanelLibrary.setLayout(new BorderLayout());
            jPanelLibrary.add(getJSplitPane(), java.awt.BorderLayout.NORTH);
            jPanelLibrary.add(getJPanelLibrarySouth(), java.awt.BorderLayout.SOUTH);
            jPanelLibrary.add(getJPanelLibraryCenter(), java.awt.BorderLayout.CENTER);
            jPanelLibrary.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentShown(java.awt.event.ComponentEvent e) {
                    initLibraries(moduleKey);
                }
            });
        }
        return jPanelLibrary;
    }

    /**
     * This method initializes jScrollPaneTablePcd
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneTablePcd() {
        if (jScrollPaneTablePcd == null) {
            jScrollPaneTablePcd = new JScrollPane();
            jScrollPaneTablePcd.setViewportView(getJTablePcd());
        }
        return jScrollPaneTablePcd;
    }

    /**
     * This method initializes jTable	
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getJTablePcd() {
        if (jTablePcd == null) {
            model = new IDefaultTableModel();
            jTablePcd = new JTable(model);
            jTablePcd.setRowHeight(20);
            jTablePcd.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
            model.addColumn("CName");
            model.addColumn("TokenSpaceGUID");
            model.addColumn("ItemType");
            model.addColumn("Token");
            model.addColumn("MaxDatumSize");
            model.addColumn("DataType");
            model.addColumn("DefaultValue");
            
            jTablePcd.getColumnModel().getColumn(0).setMinWidth(250);
                        
            jTablePcd.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTablePcd.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
                public void valueChanged(ListSelectionEvent e) {
                    
                    if (e.getValueIsAdjusting()){
                        return;
                    }
                    ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                    if (lsm.isSelectionEmpty()) {
                        return;
                    }
                    else{
                        int selectedRow = lsm.getMinSelectionIndex();
                        String cName = model.getValueAt(selectedRow, 0)+"";
                        String tsGuid = model.getValueAt(selectedRow, 1)+"";
                        String itemType = model.getValueAt(selectedRow, 2)+"";
                        //
                        // array for pcd related information: helpText, itemType, moduleType.
                        //
                        String[] pcdInfo = {"", "", ""};
                        getPcdInfo(cName, tsGuid, pcdInfo);
                        jTextAreaPcdHelp.setText(pcdInfo[0]);
                        initComboBox(pcdInfo[1], pcdInfo[2]);
                        jComboBoxItemType.setSelectedItem(itemType);
                        jTextFieldMaxDatumSize.setEnabled(true);
                        jTextFieldMaxDatumSize.setVisible(true);
                        jTextFieldMaxDatumSize.setText(model.getValueAt(selectedRow, 4)+"");
                        jTextFieldPcdDefault.setEnabled(true);
                        jTextFieldPcdDefault.setText(model.getValueAt(selectedRow, 6)+"");
                        if (model.getValueAt(selectedRow, 5).equals("VOID*")) {
                            if (pcdInfo[1].equals("FEATURE_FLAG")) {
                                jTextFieldMaxDatumSize.setVisible(false);
                            }
                            else if (pcdInfo[1].equals("FIXED_AT_BUILD")) {
                                try{
                                    jTextFieldMaxDatumSize.setEnabled(false);
                                    jTextFieldMaxDatumSize.setText(ffc.setMaxSizeForPointer(model.getValueAt(selectedRow, 6)+"")+"");
                                }
                                catch(Exception except){
                                    JOptionPane.showMessageDialog(frame, "Unacceptable PCD Value: " + except.getMessage());
                                }
                            }
                            else{
                                jTextFieldMaxDatumSize.setText(model.getValueAt(selectedRow, 4)+"");
                            }
                        }
                        else {
                            jTextFieldMaxDatumSize.setEnabled(false);
                        }
                        
                        if (!model.getValueAt(selectedRow, 2).equals("DYNAMIC") && !model.getValueAt(selectedRow, 2).equals("DYNAMIC_EX")) {
                            jTextFieldPcdDefault.setText(model.getValueAt(selectedRow, 6)+"");
                            if (model.getValueAt(selectedRow, 2).equals("FEATURE_FLAG")){
                                jTextFieldPcdDefault.setVisible(false);
                                jComboBoxFeatureFlagValue.setVisible(true);
                                jComboBoxFeatureFlagValue.setSelectedItem(model.getValueAt(selectedRow, 6)+"");
                            }
                            else{
                                jTextFieldPcdDefault.setVisible(true);
                                jTextFieldPcdDefault.setEnabled(true);
                                jComboBoxFeatureFlagValue.setVisible(false);
                            }
                        }
                        else{
                            jTextFieldPcdDefault.setEnabled(false);
                        }
                    }
                    
                    
                }
            });
            
        }
        return jTablePcd;
    }
    
    private void initComboBox(String originalType, String mType) {
        jComboBoxItemType.removeAllItems();
        jComboBoxItemType.addItem(originalType);
        if (originalType.equals("PATCHABLE_IN_MODULE") && mType.equalsIgnoreCase("false")) {
            jComboBoxItemType.addItem("FIXED_AT_BUILD");
        }
        if (originalType.equals("DYNAMIC")) {
            jComboBoxItemType.addItem("FIXED_AT_BUILD");
            jComboBoxItemType.addItem("PATCHABLE_IN_MODULE");
        }
    }
    
    /**
     * @param cName
     * @param tsGuid
     * @param sa sa[0]: HelpText; sa[1]: itemType in Msa; sa[2]: isBinary;
     */
    private void getPcdInfo(String cName, String tsGuid, String[] sa) {
        String[][] saa = new String[ffc.getLibraryInstancesCount(moduleKey)][5];
        ffc.getLibraryInstances(moduleKey, saa);
        
        try{
            if (ffc.getPcdBuildDataInfo(WorkspaceProfile.getModuleId(moduleKey), cName, tsGuid, sa)) {
                return;
            }
            for (int j = 0; j < saa.length; ++j) {
                if (ffc.getPcdBuildDataInfo(WorkspaceProfile.getModuleId(saa[j][1] + " " + saa[j][2] + " " + saa[j][3] + " " + saa[j][4]),
                                            cName, tsGuid, sa)) {
                    return;
                }
            }
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Get PCD details fail: " + e.getMessage());
        }
    }

    /**
     * This method initializes jPanelPcdSouth
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelPcdSouth() {
        if (jPanelPcdSouth == null) {
            jLabelPcdHelp = new JLabel();
            jLabelPcdHelp.setText("PCD Description");
            jPanelPcdSouth = new JPanel();
            jPanelPcdSouth.setPreferredSize(new java.awt.Dimension(607,200));
            jPanelPcdSouth.add(jLabelPcdHelp, null);
            jPanelPcdSouth.add(getJScrollPanePcdHelp(), null);
            jPanelPcdSouth.add(getJPanelPcdFields(), null);
        }
        return jPanelPcdSouth;
    }

    /**
     * This method initializes jScrollPanePcdHelp
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPanePcdHelp() {
        if (jScrollPanePcdHelp == null) {
            jScrollPanePcdHelp = new JScrollPane();
            jScrollPanePcdHelp.setPreferredSize(new java.awt.Dimension(500,100));
            jScrollPanePcdHelp.setViewportView(getJTextAreaPcdHelp());
        }
        return jScrollPanePcdHelp;
    }

    /**
     * This method initializes jTextAreaPcdHelp
     * 	
     * @return javax.swing.JTextArea	
     */
    private JTextArea getJTextAreaPcdHelp() {
        if (jTextAreaPcdHelp == null) {
            jTextAreaPcdHelp = new JTextArea();
            jTextAreaPcdHelp.setEditable(false);
        }
        return jTextAreaPcdHelp;
    }

    /**
     * This method initializes jPanelContentPaneSouth
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelContentPaneSouth() {
        if (jPanelContentPaneSouth == null) {
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setAlignment(java.awt.FlowLayout.RIGHT);
            jPanelContentPaneSouth = new JPanel();
            jPanelContentPaneSouth.setLayout(flowLayout);
            jPanelContentPaneSouth.add(getJButtonOk(), null);
            jPanelContentPaneSouth.add(getJButtonCancel(), null);
        }
        return jPanelContentPaneSouth;
    }

    /**
     * This method initializes jSplitPane	
     * 	
     * @return javax.swing.JSplitPane	
     */
    private JSplitPane getJSplitPane() {
        if (jSplitPane == null) {
            jSplitPane = new JSplitPane();
            jSplitPane.setDividerLocation(200);
            jSplitPane.setLeftComponent(getJPanelLibraryLeft());
            jSplitPane.setRightComponent(getJPanelLibraryRight());
            jSplitPane.setPreferredSize(new java.awt.Dimension(202,200));
        }
        return jSplitPane;
    }

    /**
     * This method initializes jPanelLibraryLeft
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelLibraryLeft() {
        if (jPanelLibraryLeft == null) {
            jLabelLibClass = new JLabel();
            jLabelLibClass.setText("Library Classes Uninstantiated");
            jPanelLibraryLeft = new JPanel();
            jPanelLibraryLeft.add(jLabelLibClass, null);
            jPanelLibraryLeft.add(getJScrollPaneLibClass(), null);
        }
        return jPanelLibraryLeft;
    }

    /**
     * This method initializes jPanelLibraryRight
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelLibraryRight() {
        if (jPanelLibraryRight == null) {
            jLabelQualifiedInstance = new JLabel();
            jLabelQualifiedInstance.setText("Instances Available");
            jPanelLibraryRight = new JPanel();
            jPanelLibraryRight.add(jLabelQualifiedInstance, null);
            jPanelLibraryRight.add(getJScrollPaneQualifiedInstance(), null);
        }
        return jPanelLibraryRight;
    }

    /**
     * This method initializes jScrollPaneSelectedInstances
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneSelectedInstances() {
        if (jScrollPaneSelectedInstances == null) {
            jScrollPaneSelectedInstances = new JScrollPane();
            jScrollPaneSelectedInstances.setPreferredSize(new java.awt.Dimension(600,150));
            jScrollPaneSelectedInstances.setViewportView(getJTableSelectedInstances());
        }
        return jScrollPaneSelectedInstances;
    }

    /**
     * This method initializes jTableSelectedInstances
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getJTableSelectedInstances() {
        if (jTableSelectedInstances == null) {
            selectedInstancesTableModel = new IDefaultTableModel();
            selectedInstancesTableModel.addColumn("Name");
            selectedInstancesTableModel.addColumn("ModuleGUID");
            selectedInstancesTableModel.addColumn("ModuleVersion");
            selectedInstancesTableModel.addColumn("PackageGUID");
            selectedInstancesTableModel.addColumn("PackageVersion");
            jTableSelectedInstances = new JTable(selectedInstancesTableModel);
            jTableSelectedInstances.setRowHeight(20);
            
            jTableSelectedInstances.getColumnModel().getColumn(0).setMinWidth(250);
            
            jTableSelectedInstances.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
            jTableSelectedInstances.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
        }
        return jTableSelectedInstances;
    }

    /**
     * This method initializes jScrollPaneLibClass
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneLibClass() {
        if (jScrollPaneLibClass == null) {
            jScrollPaneLibClass = new JScrollPane();
            jScrollPaneLibClass.setPreferredSize(new java.awt.Dimension(200,170));
            jScrollPaneLibClass.setViewportView(getJTableLibClass());
        }
        return jScrollPaneLibClass;
    }

    /**
     * This method initializes jTableLibClass
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getJTableLibClass() {
        if (jTableLibClass == null) {
            libClassTableModel = new IDefaultTableModel();
            libClassTableModel.addColumn("LibraryClass");
            libClassTableModel.addColumn("Arch");
            libClassTableModel.addColumn("ModType");
            jTableLibClass = new JTable(libClassTableModel);
            jTableLibClass.setRowHeight(20);
            jTableLibClass.setShowGrid(false);
            jTableLibClass.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
            TableColumn column = jTableLibClass.getColumnModel().getColumn(1);
            jTableLibClass.getColumnModel().removeColumn(column);
            column = jTableLibClass.getColumnModel().getColumn(1);
            jTableLibClass.getColumnModel().removeColumn(column);
            
            jTableLibClass.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()){
                        return;
                    }
                    ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                    if (lsm.isSelectionEmpty()) {
                        return;
                    }
                    else{
                        int selectedRow2 = lsm.getMinSelectionIndex();
                        if (selectedRow2 < 0) {
                            return;
                        }
                        //
                        // display potential lib instances according to class selection
                        //
                        libInstanceTableModel.setRowCount(0);
                        String cls = libClassTableModel.getValueAt(selectedRow2, 0).toString();
                        String arch = libClassTableModel.getValueAt(selectedRow2, 1).toString();
                        String modType = libClassTableModel.getValueAt(selectedRow2, 2).toString();
                        ArrayList<String> al = classInstanceMap.get(new LibraryClassDescriptor(cls, arch, modType));
                        if (al == null) {
                            return;
                        }
                        ListIterator<String> li = al.listIterator();
                        while(li.hasNext()) {
                            String instance = li.next();
                            String[] s = {"", "", "", "", ""};
                            if (WorkspaceProfile.getModuleId(instance) != null) {
                                s[0] = WorkspaceProfile.getModuleId(instance).getName();
                            }
                            
                            String[] instancePart = instance.split(" ");
                            for (int i = 0; i < instancePart.length; ++i){
                                s[i+1] = instancePart[i];
                            }
                            libInstanceTableModel.addRow(s);
                        }
                        
                    }
                }
            });
        }
        return jTableLibClass;
    }

    /**
     * This method initializes jScrollPaneQualifiedInstance
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneQualifiedInstance() {
        if (jScrollPaneQualifiedInstance == null) {
            jScrollPaneQualifiedInstance = new JScrollPane();
            jScrollPaneQualifiedInstance.setPreferredSize(new java.awt.Dimension(600,170));
            jScrollPaneQualifiedInstance.setViewportView(getJTableLibInstances());
        }
        return jScrollPaneQualifiedInstance;
    }

    /**
     * This method initializes jTableLibInstances
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getJTableLibInstances() {
        if (jTableLibInstances == null) {
            libInstanceTableModel = new IDefaultTableModel();
            libInstanceTableModel.addColumn("Name");
            libInstanceTableModel.addColumn("ModuleGUID");
            libInstanceTableModel.addColumn("ModuleVersion");
            libInstanceTableModel.addColumn("PackageGUID");
            libInstanceTableModel.addColumn("PackageVersion");
            jTableLibInstances = new JTable(libInstanceTableModel);
            jTableLibInstances.setRowHeight(20);
            
            jTableLibInstances.getColumnModel().getColumn(0).setMinWidth(250);
            
            jTableLibInstances.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
            jTableLibInstances.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            
        }
        return jTableLibInstances;
    }

    /**
     * This method initializes jPanelLibrarySouth
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelLibrarySouth() {
        if (jPanelLibrarySouth == null) {
            jPanelLibrarySouth = new JPanel();
        }
        return jPanelLibrarySouth;
    }

    /**
     * This method initializes jPanelLibraryCenter
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelLibraryCenter() {
        if (jPanelLibraryCenter == null) {
            jLabelInstanceHelp = new JLabel();
            jLabelInstanceHelp.setText("Instance Description");
            jLabelSelectedInstances = new JLabel();
            jLabelSelectedInstances.setText("Selected Instances");
            jPanelLibraryCenter = new JPanel();
            jPanelLibraryCenter.setLayout(new BorderLayout());

            jPanelLibraryCenter.add(getJPanelLibraryCenterC(), java.awt.BorderLayout.CENTER);
            jPanelLibraryCenter.add(getJPanelLibraryCenterN(), java.awt.BorderLayout.NORTH);

        }
        return jPanelLibraryCenter;
    }

    /**
     * This method initializes jScrollPaneInstanceHelp
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneInstanceHelp() {
        if (jScrollPaneInstanceHelp == null) {
            jScrollPaneInstanceHelp = new JScrollPane();
            jScrollPaneInstanceHelp.setPreferredSize(new java.awt.Dimension(400,50));
            jScrollPaneInstanceHelp.setViewportView(getJTextAreaInstanceHelp());
        }
        return jScrollPaneInstanceHelp;
    }

    /**
     * This method initializes jTextAreaInstanceHelp
     * 	
     * @return javax.swing.JTextArea	
     */
    private JTextArea getJTextAreaInstanceHelp() {
        if (jTextAreaInstanceHelp == null) {
            jTextAreaInstanceHelp = new JTextArea();
            jTextAreaInstanceHelp.setEditable(false);
        }
        return jTextAreaInstanceHelp;
    }

    /**
     * This method initializes jButtonAdd
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonAdd() {
        if (jButtonAdd == null) {
            jButtonAdd = new JButton();
            jButtonAdd.setPreferredSize(new java.awt.Dimension(80,20));
            jButtonAdd.setText("Add");
            jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int row = jTableLibInstances.getSelectedRow();
                    if (row < 0) {
                        return;
                    }
                    
                    String instanceValue = libInstanceTableModel.getValueAt(row, 1) + " " +
                    libInstanceTableModel.getValueAt(row, 2) + " " +
                    libInstanceTableModel.getValueAt(row, 3) + " " +
                    libInstanceTableModel.getValueAt(row, 4);
                    ModuleIdentification libMi = WorkspaceProfile.getModuleId(instanceValue);
                    try {
                        addLibInstance (libMi);
                    }
                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(frame, "Adding Instance" + libMi.getName() + ": "+ exception.getMessage());
                        return;
                    }
                    docConsole.setSaved(false);
                    Object[] s = {libInstanceTableModel.getValueAt(row, 0), libInstanceTableModel.getValueAt(row, 1),
                                  libInstanceTableModel.getValueAt(row, 2), libInstanceTableModel.getValueAt(row, 3),
                                  libInstanceTableModel.getValueAt(row, 4)};
                    selectedInstancesTableModel.addRow(s);
                    try {
                        resolveLibraryInstances(instanceValue);
                    }
                    catch (Exception exp) {
                        JOptionPane.showMessageDialog(frame, exp.getMessage());
                    }
                    showClassToResolved();
                }
            });
        }
        return jButtonAdd;
    }

    /**
     * This method initializes jButton1
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonDeleteInstance() {
        if (jButtonDeleteInstance == null) {
            jButtonDeleteInstance = new JButton();
            jButtonDeleteInstance.setPreferredSize(new java.awt.Dimension(80,20));
            jButtonDeleteInstance.setText("Delete");
            jButtonDeleteInstance.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int row = jTableSelectedInstances.getSelectedRow();
                    if (row < 0) {
                        return;
                    }
                    docConsole.setSaved(false);
                    removeInstance(selectedInstancesTableModel.getValueAt(row, 1) + " " +
                                   selectedInstancesTableModel.getValueAt(row, 2) + " " +
                                   selectedInstancesTableModel.getValueAt(row, 3) + " " +
                                   selectedInstancesTableModel.getValueAt(row, 4));
                    ffc.removeLibraryInstance(moduleKey, row);
                    selectedInstancesTableModel.removeRow(row);
                    
                }
            });
        }
        return jButtonDeleteInstance;
    }

    /**
     * This method initializes jButton2	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonOk() {
        if (jButtonOk == null) {
            jButtonOk = new JButton();
            jButtonOk.setPreferredSize(new java.awt.Dimension(80,20));
            jButtonOk.setText("Close");
            jButtonOk.addActionListener(this);
        }
        return jButtonOk;
    }

    /**
     * This method initializes jButton3	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonCancel() {
        if (jButtonCancel == null) {
            jButtonCancel = new JButton();
            jButtonCancel.setPreferredSize(new java.awt.Dimension(80,20));
            jButtonCancel.setText("Cancel");
            jButtonCancel.setVisible(false);
        }
        return jButtonCancel;
    }
    public void actionPerformed(ActionEvent arg0) {

        if (arg0.getSource() == jButtonOk) {
            if (jTableModuleSaOptions.isEditing()) {
                jTableModuleSaOptions.getCellEditor().stopCellEditing();
            }
            this.setVisible(false);
        }
    }
    /**
     * This method initializes jPanelModuleSaOpts
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelModuleSaOpts() {
        if (jPanelModuleSaOpts == null) {
            FlowLayout flowLayout4 = new FlowLayout();
            flowLayout4.setAlignment(java.awt.FlowLayout.LEFT);
            jLabelFfsFormatKey = new JLabel();
            jLabelFfsFormatKey.setText("FFS Format Key");
            jLabelFfsFormatKey.setPreferredSize(new java.awt.Dimension(90,16));
            jLabelFfsFileGuid = new JLabel();
            jLabelFfsFileGuid.setText("FFS File GUID");
            jLabelFfsFileGuid.setPreferredSize(new java.awt.Dimension(90,16));
            jLabelFfsFileGuid.setVisible(false);
            jLabelFvBinding = new JLabel();
            jLabelFvBinding.setText("FV Binding");
            jLabelFvBinding.setPreferredSize(new java.awt.Dimension(90,16));
            jPanelModuleSaOpts = new JPanel();
            jPanelModuleSaOpts.setLayout(flowLayout4);
            jPanelModuleSaOpts.add(jLabelFvBinding, null);
            jPanelModuleSaOpts.add(getJTextFieldFvBinding(), null);
            jPanelModuleSaOpts.add(jLabelFfsFileGuid, null);
            jPanelModuleSaOpts.add(getJTextFieldFileGuid(), null);
            jPanelModuleSaOpts.add(jLabelFfsFormatKey, null);
            jPanelModuleSaOpts.add(getJTextFieldFfsKey(), null);
            jPanelModuleSaOpts.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentShown(java.awt.event.ComponentEvent e) {
                    initFvInfo(moduleKey);
                }
            });
        }
        return jPanelModuleSaOpts;
    }
    
    private Vector<String> getVectorFromString (String s) {
        if (s == null || s.equals("null")) {
            s = "";
        }
        String[] sa1 = s.split(" ");
        Vector<String> v = new Vector<String>();
        for (int i = 0; i < sa1.length; ++i) {
            v.add(sa1[i]);
        }
        return v;
    }
    
    /**
     * This method initializes jTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldFvBinding() {
        if (jTextFieldFvBinding == null) {
            jTextFieldFvBinding = new JTextField();
            jTextFieldFvBinding.setPreferredSize(new java.awt.Dimension(400,20));
            jTextFieldFvBinding.setEditable(true);
            jTextFieldFvBinding.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent e) {
                    String originalFvBinding = ffc.getFvBinding(moduleKey);
                    String newFvBinding = jTextFieldFvBinding.getText();
                    if (newFvBinding.equals(originalFvBinding)) {
                        return;
                    }
                    if (newFvBinding.length() == 0 && originalFvBinding == null) {
                        return;
                    }
                    
                    Vector<String> oldFvList = getVectorFromString (originalFvBinding);
                    Vector<String> newFvList = getVectorFromString (newFvBinding);
                    String moduleInfo[] = moduleKey.split(" ");
                    ffc.setFvBinding(moduleKey, newFvBinding);
                    //
                    // remove module from Fvs that not in newFvList now.
                    //
                    oldFvList.removeAll(newFvList);
                    for (int j = 0; j < oldFvList.size(); ++j) {
                        ffc.removeModuleInBuildOptionsUserExtensions(oldFvList.get(j), moduleInfo[0], moduleInfo[1], moduleInfo[2], moduleInfo[3], moduleInfo[4]);    
                    }
                    //
                    // add module to Fvs that were not in oldFvList.
                    //
                    oldFvList = getVectorFromString (originalFvBinding);
                    newFvList.removeAll(oldFvList);
                    for (int i = 0; i < newFvList.size(); ++i) {
                        ffc.addModuleIntoBuildOptionsUserExtensions(newFvList.get(i), moduleInfo[0], moduleInfo[1], moduleInfo[2], moduleInfo[3], moduleInfo[4]);
                    }
                    docConsole.setSaved(false);
                }
            });
            
        }
        return jTextFieldFvBinding;
    }
    /**
     * This method initializes jTextField1	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldFileGuid() {
        if (jTextFieldFileGuid == null) {
            jTextFieldFileGuid = new JTextField();
            jTextFieldFileGuid.setPreferredSize(new java.awt.Dimension(300,20));
            jTextFieldFileGuid.setVisible(false);
            jTextFieldFileGuid.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent e) {
                    String originalFileGuid = ffc.getFfsFileNameGuid(moduleKey);
                    String newFileGuid = jTextFieldFileGuid.getText();
                    if (newFileGuid.equals(originalFileGuid)) {
                        return;
                    }
                    if (newFileGuid.length() == 0 && originalFileGuid == null) {
                        return;
                    }
                    if (newFileGuid.length() > 0) {
                        if (!DataValidation.isGuid(newFileGuid)) {
                            JOptionPane.showMessageDialog(frame, "FFS File Guid is NOT GUID Type.");
                            return;
                        }
                    }
                    
                    docConsole.setSaved(false);
                    if (newFileGuid.length() == 0) {
                        newFileGuid = null;
                    }
                    ffc.setFfsFileNameGuid(moduleKey, newFileGuid);
                }
            });
            
        }
        return jTextFieldFileGuid;
    }
    /**
     * This method initializes jTextFieldFfsKey	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldFfsKey() {
        if (jTextFieldFfsKey == null) {
            jTextFieldFfsKey = new JTextField();
            jTextFieldFfsKey.setPreferredSize(new java.awt.Dimension(250,20));
            jTextFieldFfsKey.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent e) {
                    String originalFfsKey = ffc.getFfsFormatKey(moduleKey);
                    String newFfsKey = jTextFieldFfsKey.getText();
                    if (newFfsKey.equals(originalFfsKey)) {
                        return;
                    }
                    if (newFfsKey.length() == 0 && originalFfsKey == null) {
                        return;
                    }
                    docConsole.setSaved(false);
                    ffc.setFfsFormatKey(moduleKey, newFfsKey);
                }
            });
            
        }
        return jTextFieldFfsKey;
    }
    /**
     * This method initializes jScrollPaneModuleSaOptions
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneModuleSaOptions() {
        if (jScrollPaneModuleSaOptions == null) {
            jScrollPaneModuleSaOptions = new JScrollPane();
            jScrollPaneModuleSaOptions.setPreferredSize(new java.awt.Dimension(600,350));
            jScrollPaneModuleSaOptions.setViewportView(getJTableModuleSaOptions());
        }
        return jScrollPaneModuleSaOptions;
    }
    /**
     * This method initializes jTableModuleSaOptions
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getJTableModuleSaOptions() {
        if (jTableModuleSaOptions == null) {
            optionsTableModel = new DefaultTableModel();
            optionsTableModel.addColumn("BuildTargets");
            optionsTableModel.addColumn("ToolChainFamily");
            optionsTableModel.addColumn("TagName");
            optionsTableModel.addColumn("ToolCode");
            optionsTableModel.addColumn("SupportedArchs");
            optionsTableModel.addColumn("Contents");
            jTableModuleSaOptions = new JTable(optionsTableModel);
            jTableModuleSaOptions.setRowHeight(20);
            
            jTableModuleSaOptions.getColumnModel().getColumn(0).setMinWidth(buildTargetWidth);
            jTableModuleSaOptions.getColumnModel().getColumn(1).setMinWidth(toolChainFamilyWidth);
            jTableModuleSaOptions.getColumnModel().getColumn(2).setMinWidth(tagNameWidth);
            jTableModuleSaOptions.getColumnModel().getColumn(3).setMinWidth(toolCmdCodeWidth);
            jTableModuleSaOptions.getColumnModel().getColumn(4).setMinWidth(supportArchWidth);
            jTableModuleSaOptions.getColumnModel().getColumn(5).setMinWidth(argWidth);
//            javax.swing.table.TableColumn toolFamilyCol = jTableModuleSaOptions.getColumnModel().getColumn(1);
//            JComboBox cb = new JComboBox();
//            cb.addItem("MSFT");
//            cb.addItem("GCC");
//            cb.addItem("CYGWIN");
//            cb.addItem("INTEL");
//            cb.addItem("USER_DEFINED");
//            toolFamilyCol.setCellEditor(new DefaultCellEditor(cb));
            
            Vector<String> vArch = new Vector<String>();
            vArch.add("IA32");
            vArch.add("X64");
            vArch.add("IPF");
            vArch.add("EBC");
            vArch.add("ARM");
            vArch.add("PPC");
            jTableModuleSaOptions.getColumnModel().getColumn(4).setCellEditor(new ListEditor(vArch));
            
            jTableModuleSaOptions.getColumnModel().getColumn(5).setCellEditor(new LongTextEditor());
            
            jTableModuleSaOptions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTableModuleSaOptions.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            jTableModuleSaOptions.getModel().addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent arg0) {
                    // TODO Auto-generated method stub
                    int row = arg0.getFirstRow();
                    TableModel m = (TableModel)arg0.getSource();
                    
                    if (arg0.getType() == TableModelEvent.UPDATE){
                        //ToDo Data Validition check.
                        String targets = m.getValueAt(row, 0) + "";
                        Vector<Object> targetName = null;
                        if (targets.length() > 0) {
                            targetName = new Vector<Object>();
                            String[] sArray = targets.split(" ");
                            for (int i = 0; i < sArray.length; ++i) {
                                targetName.add(sArray[i]);
                            }
                        }
                        
                        String toolChain = m.getValueAt(row, 1) + "";
                        String tagName = m.getValueAt(row, 2) + "";
                        String toolCode = m.getValueAt(row, 3) + "";
                        String archs = m.getValueAt(row, 4) + "";
                        Vector<Object> supArch = null;
                        if (archs.length() > 0) {
                            supArch = new Vector<Object>();
                            String[] sArray1 = archs.split(" ");
                            for (int i = 0; i < sArray1.length; ++i) {
                                supArch.add(sArray1[i]);
                            }
                        }
                        
                        String contents = m.getValueAt(row, 5) + "";
                        docConsole.setSaved(false);
                        ffc.updateModuleSAOptionsOpt(moduleKey, row, targetName, toolChain, tagName, toolCode, supArch, contents);
                    }
                }
            });
        }
        return jTableModuleSaOptions;
    }
    /**
     * This method initializes jButtonNew
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonNew() {
        if (jButtonNew == null) {
            jButtonNew = new JButton();
            jButtonNew.setPreferredSize(new java.awt.Dimension(80,20));
            jButtonNew.setText("New");
            jButtonNew.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String[] row = {"", "", "", "", "", ""};
                    optionsTableModel.addRow(row);
                    Vector<Object> v = null;
                    Vector<Object> v1 = null;
                    docConsole.setSaved(false);
                    ffc.genModuleSAOptionsOpt(moduleKey, v, "", "", "", v1, "");
                }
            });
        }
        return jButtonNew;
    }
    /**
     * This method initializes jButtonDelete
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonDeleteOption() {
        if (jButtonDeleteOption == null) {
            jButtonDeleteOption = new JButton();
            jButtonDeleteOption.setPreferredSize(new java.awt.Dimension(80,20));
            jButtonDeleteOption.setText("Delete");
            jButtonDeleteOption.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (jTableModuleSaOptions.getSelectedRow() < 0) {
                        return;
                    }
                    docConsole.setSaved(false);
                    ffc.removeModuleSAOptionsOpt(moduleKey, jTableModuleSaOptions.getSelectedRow());
                    optionsTableModel.removeRow(jTableModuleSaOptions.getSelectedRow());
                }
            });
        }
        return jButtonDeleteOption;
    }
    
    /**
    Start the window at the center of screen
    
    **/
   protected void centerWindow(int intWidth, int intHeight) {
       Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
       this.setLocation((d.width - intWidth) / 2, (d.height - intHeight) / 2);
   }

   /**
    Start the window at the center of screen
    
    **/
   protected void centerWindow() {
       centerWindow(this.getSize().width, this.getSize().height);
   }
/**
 * This method initializes jPanelPcdFields
 * 	
 * @return javax.swing.JPanel	
 */
private JPanel getJPanelPcdFields() {
    if (jPanelPcdFields == null) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.setRows(3);
        gridLayout.setColumns(2);
        jPanelPcdFields = new JPanel();
        jPanelPcdFields.setLayout(gridLayout);
        jPanelPcdFields.setPreferredSize(new java.awt.Dimension(600,90));
        jPanelPcdFields.add(getJPanelPcdFieldsFirstRow(), null);
        jPanelPcdFields.add(getJPanelPcdFieldsSecondRow(), null);
        jPanelPcdFields.add(getJPanelPcdFieldsThirdRow(), null);
    }
    return jPanelPcdFields;
}
/**
 * This method initializes jPanelPcdFieldsSecondRow
 * 	
 * @return javax.swing.JPanel	
 */
private JPanel getJPanelPcdFieldsSecondRow() {
    if (jPanelPcdFieldsSecondRow == null) {
        FlowLayout flowLayout2 = new FlowLayout();
        flowLayout2.setAlignment(java.awt.FlowLayout.LEFT);
        jLabelMaxDatumSize = new JLabel();
        jLabelMaxDatumSize.setText("Max Datum Size");
        jPanelPcdFieldsSecondRow = new JPanel();
        jPanelPcdFieldsSecondRow.setLayout(flowLayout2);
        jPanelPcdFieldsSecondRow.add(jLabelMaxDatumSize, null);
        jPanelPcdFieldsSecondRow.add(getJTextFieldMaxDatumSize(), null);
    }
    return jPanelPcdFieldsSecondRow;
}
/**
 * This method initializes jPanelPcdFieldsThirdRow
 * 	
 * @return javax.swing.JPanel	
 */
private JPanel getJPanelPcdFieldsThirdRow() {
    if (jPanelPcdFieldsThirdRow == null) {
        FlowLayout flowLayout3 = new FlowLayout();
        flowLayout3.setAlignment(java.awt.FlowLayout.LEFT);
        jLabelPcdDefaultValue = new JLabel();
        jLabelPcdDefaultValue.setText("Default Value");
        jLabelPcdDefaultValue.setPreferredSize(new java.awt.Dimension(91,16));
        jPanelPcdFieldsThirdRow = new JPanel();
        jPanelPcdFieldsThirdRow.setLayout(flowLayout3);
        jPanelPcdFieldsThirdRow.add(jLabelPcdDefaultValue, null);
        jPanelPcdFieldsThirdRow.add(getJTextFieldPcdDefault(), null);
        jPanelPcdFieldsThirdRow.add(getJComboBoxFeatureFlagValue(), null);
        jPanelPcdFieldsThirdRow.add(getJButtonUpdatePcd(), null);
    }
    return jPanelPcdFieldsThirdRow;
}
/**
 * This method initializes jPanelPcdFieldsFirstRow
 * 	
 * @return javax.swing.JPanel	
 */
private JPanel getJPanelPcdFieldsFirstRow() {
    if (jPanelPcdFieldsFirstRow == null) {
        FlowLayout flowLayout1 = new FlowLayout();
        flowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
        jLabelItemType = new JLabel();
        jLabelItemType.setText("Item Type");
        jLabelItemType.setPreferredSize(new java.awt.Dimension(91,16));
        jPanelPcdFieldsFirstRow = new JPanel();
        jPanelPcdFieldsFirstRow.setLayout(flowLayout1);
        jPanelPcdFieldsFirstRow.add(jLabelItemType, null);
        jPanelPcdFieldsFirstRow.add(getJComboBoxItemType(), null);
    }
    return jPanelPcdFieldsFirstRow;
}
/**
 * This method initializes jComboBoxItemType
 * 	
 * @return javax.swing.JComboBox	
 */
private JComboBox getJComboBoxItemType() {
    if (jComboBoxItemType == null) {
        jComboBoxItemType = new JComboBox();
        jComboBoxItemType.setPreferredSize(new java.awt.Dimension(200,20));
        jComboBoxItemType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                int row = jTablePcd.getSelectedRow();
                if (row < 0 || model.getValueAt(row, 2).equals(jComboBoxItemType.getSelectedItem())) {
                    return;
                }
                if (jComboBoxItemType.getItemCount() == 3) {
                    if (!jComboBoxItemType.getSelectedItem().equals("DYNAMIC")) {
                        
                        if (jComboBoxItemType.getSelectedItem().equals("FIXED_AT_BUILD")) {
                            jTextFieldPcdDefault.setText("");
                            jTextFieldPcdDefault.setEnabled(true);
                        }
                    }
                    else{
                        
                    }
                }
            }
        });
    }
    return jComboBoxItemType;
}

private void pcdDynamicToNonDynamic(String cName, String tsGuid) {
    String[][] saa = new String[ffc.getDynamicPcdBuildDataCount()][5];
    ffc.getDynamicPcdBuildData(saa);
    String maxSize = "";
    String value = "";
    for (int i = 0; i < saa.length; ++i) {
        if (saa[i][0].equals(cName) && saa[i][2].equals(tsGuid)) {
            maxSize = saa[i][3];
            value = ffc.getDynamicPcdBuildDataValue(i);
            break;
        }
    }
    
    ArrayList<String> al = ffc.getDynPcdMapValue(cName + " " + tsGuid);
    for (int i = 0; i < al.size(); ++i) {
        String mKey = moduleInfo (al.get(i));
        value = null;
        ffc.updatePcdData(mKey, cName, tsGuid, jComboBoxItemType.getSelectedItem()+"", maxSize, value);
        String itemType = jComboBoxItemType.getSelectedItem()+"";
        al.set(i, mKey + " " + itemType);
    }
    
    ffc.removeDynamicPcdBuildData(cName, tsGuid);
}

private void pcdNonDynamicToDynamic(String cName, String tsGuid) {
    ArrayList<String> al = ffc.getDynPcdMapValue(cName + " " + tsGuid);
    for (int i = 0; i < al.size(); ++i) {
        String mKey = moduleInfo (al.get(i));
        ffc.updatePcdData(mKey, cName, tsGuid, jComboBoxItemType.getSelectedItem()+"", jTextFieldMaxDatumSize.getText(), jTextFieldPcdDefault.isVisible() ? jTextFieldPcdDefault.getText() : jComboBoxFeatureFlagValue.getSelectedItem()+"");
        String itemType = jComboBoxItemType.getSelectedItem()+"";
        al.set(i, mKey + " " + itemType);
    }
    try{
        ffc.addDynamicPcdBuildData(cName, jTablePcd.getValueAt(jTablePcd.getSelectedRow(), 3), tsGuid, "DYNAMIC", jTablePcd.getValueAt(jTablePcd.getSelectedRow(), 5)+"", jTextFieldPcdDefault.isVisible() ? jTextFieldPcdDefault.getText() : jComboBoxFeatureFlagValue.getSelectedItem()+"");
    }
    catch(Exception e){
        JOptionPane.showMessageDialog(frame, "PCD value format: " + e.getMessage());
    }
}

private String moduleInfo (String pcdInfo) {
    
    return pcdInfo.substring(0, pcdInfo.lastIndexOf(" "));
}

/**
 * This method initializes jTextFieldMaxDatumSize
 * 	
 * @return javax.swing.JTextField	
 */
private JTextField getJTextFieldMaxDatumSize() {
    if (jTextFieldMaxDatumSize == null) {
        jTextFieldMaxDatumSize = new JTextField();
        jTextFieldMaxDatumSize.setPreferredSize(new java.awt.Dimension(200,20));
    }
    return jTextFieldMaxDatumSize;
}
/**
 * This method initializes jTextField4	
 * 	
 * @return javax.swing.JTextField	
 */
private JTextField getJTextFieldPcdDefault() {
    if (jTextFieldPcdDefault == null) {
        jTextFieldPcdDefault = new JTextField();
        jTextFieldPcdDefault.setPreferredSize(new java.awt.Dimension(200,20));
    }
    return jTextFieldPcdDefault;
}
/**
 * This method initializes jButton6	
 * 	
 * @return javax.swing.JButton	
 */
private JButton getJButtonUpdatePcd() {
    if (jButtonUpdatePcd == null) {
        jButtonUpdatePcd = new JButton();
        jButtonUpdatePcd.setPreferredSize(new java.awt.Dimension(150,20));
        jButtonUpdatePcd.setText("Update PCD Data");
        jButtonUpdatePcd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                int row = jTablePcd.getSelectedRow();
                if (row < 0) {
                    return;
                }
                docConsole.setSaved(false);
                String oldItemType = model.getValueAt(row, 2)+"";
                String newItemType = jComboBoxItemType.getSelectedItem()+"";
                model.setValueAt(newItemType, row, 2);
                model.setValueAt(jTextFieldPcdDefault.isVisible()? jTextFieldPcdDefault.getText():jComboBoxFeatureFlagValue.getSelectedItem(), row, 6);
                
                String[] pcdInfo = {"", "", ""};
                getPcdInfo (model.getValueAt(row, 0)+"", model.getValueAt(row, 1)+"", pcdInfo);
                if (pcdInfo[1].equals("FIXED_AT_BUILD") && model.getValueAt(row, 5).equals("VOID*")) {
                    try {
                        jTextFieldMaxDatumSize.setText(ffc.setMaxSizeForPointer(model.getValueAt(row, 6)+"")+"");
                    }
                    catch (Exception exp) {
                        JOptionPane.showMessageDialog(frame, "PCD Value MalFormed: " + exp.getMessage());
                        return;
                    }
                }
                model.setValueAt(jTextFieldMaxDatumSize.getText(), row, 4);
                
                if (oldItemType.equals("DYNAMIC") && !newItemType.equals("DYNAMIC")) {
                    pcdDynamicToNonDynamic(model.getValueAt(row, 0)+"", model.getValueAt(row, 1)+"");
                }
                if (!oldItemType.equals("DYNAMIC") && newItemType.equals("DYNAMIC")) {
                    pcdNonDynamicToDynamic(model.getValueAt(row, 0)+"", model.getValueAt(row, 1)+"");
                }
                ffc.updatePcdData(moduleKey, model.getValueAt(row, 0)+"", model.getValueAt(row, 1)+"", model.getValueAt(row, 2)+"", model.getValueAt(row, 4)+"", model.getValueAt(row, 6)+"");
            }
        });
    }
    return jButtonUpdatePcd;
}
/**
 * This method initializes jComboBoxFeatureFlagValue
 * 	
 * @return javax.swing.JComboBox	
 */
private JComboBox getJComboBoxFeatureFlagValue() {
    if (jComboBoxFeatureFlagValue == null) {
        jComboBoxFeatureFlagValue = new JComboBox();
        jComboBoxFeatureFlagValue.setPreferredSize(new java.awt.Dimension(100,20));
        jComboBoxFeatureFlagValue.setVisible(false);
        jComboBoxFeatureFlagValue.addItem("TRUE");
        jComboBoxFeatureFlagValue.addItem("FALSE");
    }
    return jComboBoxFeatureFlagValue;
}
/**
 * This method initializes jPanelCustomToolChain	
 * 	
 * @return javax.swing.JPanel	
 */
private JPanel getJPanelCustomToolChain() {
    if (jPanelCustomToolChain == null) {
        jPanelCustomToolChain = new JPanel();
        jPanelCustomToolChain.setLayout(new BorderLayout());
        jPanelCustomToolChain.add(getJPanelToolchainS(), java.awt.BorderLayout.SOUTH);
        jPanelCustomToolChain.add(getJScrollPaneModuleSaOptions(), java.awt.BorderLayout.CENTER);
        jPanelCustomToolChain.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent e) {
                initToolChainOptions(moduleKey);
            }
        });
    }
    return jPanelCustomToolChain;
}
/**
 * This method initializes jPanelToolchainS	
 * 	
 * @return javax.swing.JPanel	
 */
private JPanel getJPanelToolchainS() {
    if (jPanelToolchainS == null) {
        jPanelToolchainS = new JPanel();
        jPanelToolchainS.add(getJButtonNew(), null);
        jPanelToolchainS.add(getJButtonDeleteOption(), null);
    }
    return jPanelToolchainS;
}

/**
 * This method initializes jPanelLibraryCenterN	
 * 	
 * @return javax.swing.JPanel	
 */
private JPanel getJPanelLibraryCenterN() {
    if (jPanelLibraryCenterN == null) {
        FlowLayout flowLayout5 = new FlowLayout();
        flowLayout5.setAlignment(java.awt.FlowLayout.CENTER);
        flowLayout5.setHgap(10);
        jPanelLibraryCenterN = new JPanel();
        jPanelLibraryCenterN.setLayout(flowLayout5);
        jPanelLibraryCenterN.add(jLabelInstanceHelp, null);
        jPanelLibraryCenterN.add(getJScrollPaneInstanceHelp(), null);
        jPanelLibraryCenterN.add(getJButtonAdd(), null);
        jPanelLibraryCenterN.add(getJButtonDeleteInstance(), null);
    }
    return jPanelLibraryCenterN;
}
/**
 * This method initializes jPanelLibraryCenterC	
 * 	
 * @return javax.swing.JPanel	
 */
private JPanel getJPanelLibraryCenterC() {
    if (jPanelLibraryCenterC == null) {
        jPanelLibraryCenterC = new JPanel();
        jPanelLibraryCenterC.add(jLabelSelectedInstances, null);
        jPanelLibraryCenterC.add(getJScrollPaneSelectedInstances(), null);
    }
    return jPanelLibraryCenterC;
}


}  //  @jve:decl-index=0:visual-constraint="10,10"

class MultipleInstanceException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -9148463005930920297L;
    private String className = null;
    private String libInstance1 = null;
    private String libInstance2 = null;
    
    MultipleInstanceException (String libClass, String instance1, String instance2) {
        super();
        className = libClass;
        libInstance1 = instance1;
        libInstance2 = instance2;
    }

    /* (non-Javadoc)
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return "Library Class " + className + "is Produced by Two Instances: " 
            + libInstance1 + " and " + libInstance2 + ". Platform Build will Fail.";
    }
    
}

class NoInstanceException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1987122786598970598L;
    
    private String className = null;
    
    NoInstanceException (String libClass) {
        className = libClass;
    }
    
    public String getMessage() {
        return "No Applicable Instance for Library Class " + className
            + ", Platform Build will Fail.";
    }
}
