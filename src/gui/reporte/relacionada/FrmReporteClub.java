package gui.reporte.relacionada;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import entidad.Club;
import model.ClubModel;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.swing.JRViewer;
import util.FechaUtil;
import util.GeneradorReporte;
import util.Validaciones;

public class FrmReporteClub extends JFrame implements ActionListener   {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtInicio;
	private JTextField txtFin;
	private JButton btnFiltrar;
	private JPanel panelReporte;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
					FrmReporteClub frame = new FrmReporteClub();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrmReporteClub() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1206, 714);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitulo = new JLabel("Reporte de club");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblTitulo.setBounds(10, 26, 1161, 36);
		contentPane.add(lblTitulo);
		
		panelReporte = new JPanel();
		panelReporte.setBorder(new TitledBorder(null, "Reportes", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panelReporte.setBounds(31, 176, 1144, 475);
		contentPane.add(panelReporte);
		panelReporte.setLayout(new BorderLayout());
		
		JLabel lblFechaInicio = new JLabel("Fecha Inicio");
		lblFechaInicio.setBounds(31, 126, 81, 14);
		contentPane.add(lblFechaInicio);
		
		txtInicio = new JTextField();
		txtInicio.setColumns(10);
		txtInicio.setBounds(131, 123, 119, 20);
		contentPane.add(txtInicio);
		
		JLabel lblFechaFin = new JLabel("Fecha Fin");
		lblFechaFin.setBounds(322, 126, 90, 14);
		contentPane.add(lblFechaFin);
		
		txtFin = new JTextField();
		txtFin.setColumns(10);
		txtFin.setBounds(422, 123, 109, 20);
		contentPane.add(txtFin);
		
		btnFiltrar = new JButton("Filtrar");
		btnFiltrar.addActionListener(this);
		btnFiltrar.setBounds(592, 122, 162, 23);
		contentPane.add(btnFiltrar);
		
		
	}	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnFiltrar) {
			actionPerformedBtnFiltrarJButton(e);
		}
	}
	protected void actionPerformedBtnFiltrarJButton(ActionEvent e) {
		String fecIni  = txtInicio.getText().trim();
		String fecFin  = txtFin.getText().trim();
		
		if (!fecIni.matches(Validaciones.FECHA)) {
			mensaje("La fecha Inicio tiene formato yyyy-MM-dd");
		}else if (!fecFin.matches(Validaciones.FECHA)) {
			mensaje("La fecha Fin tiene formato yyyy-MM-dd");
		}else if (FechaUtil.isNotSuperiorFechaYYYYMMdd(fecIni, fecFin)) {
			mensaje("La Fecha fin es superior a la Fecha inicio");
		}else {
			Date dtIni = Date.valueOf(fecIni);
			Date dtFin = Date.valueOf(fecFin);
			
			ClubModel model = new ClubModel();
			List<Club> lista = model.listaPorRangoFechas(dtIni, dtFin);
			
			//Datos del reporte
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);
			
			//Dise�o del reporte
			String jasper = "reporteClub.jasper";	
			
			//Se obtiene el reporte
			JasperPrint print = GeneradorReporte.genera(jasper, dataSource, null);
			
			JRViewer jRViewer = new JRViewer(print);
			
			panelReporte.removeAll();
			panelReporte.add(jRViewer);
			panelReporte.repaint();
			panelReporte.revalidate();
			
		}	
	
	}
	
	public void mensaje(String ms){
		JOptionPane.showMessageDialog(this, ms);
	}
}












