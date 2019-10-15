package co.com.comfaboy.herramientas.controller;

import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import co.com.comfaboy.herramientas.dbintegra.modelo.Timdderc;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tindcdda;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinddecd;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinddecl;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinddede;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinddemc;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tindfacr;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tindfact;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tindfada;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tindfopa;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tindrcda;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tindrcdi;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinmarbol;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinmcodi;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinmcoli;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinmcuen;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinmcxc;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinmcxp;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinmfact;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinmfopa;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinmimrc;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinmmcont;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinmreca;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinmrecad;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinmsede;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinpcodi;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinpcoli;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinpcxc;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinpcxp;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinpfact;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinpmcont;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinpreca;
import co.com.comfaboy.herramientas.dbintegra.modelo.Tinprecad;
import co.com.comfaboy.herramientas.servicio.FinanArsServicio;
import co.com.comfaboy.herramientas.servicio.FinanContService;
import co.com.comfaboy.herramientas.servicio.HtasServicio;
import co.com.comfaboy.herramientas.servicio.IntegracionServicio;
import co.com.comfaboy.herramietas.dbintegra.dto.ConsigancionLineaDTO;
import co.com.comfaboy.herramietas.dbintegra.dto.ConsignacionDirectaDTO;
import co.com.comfaboy.herramietas.dbintegra.dto.EpsDto;
import co.com.comfaboy.herramietas.dbintegra.dto.EstructuraArchMoviDTO;
import co.com.comfaboy.herramietas.dbintegra.dto.FacturasDTO;
import co.com.comfaboy.herramietas.dbintegra.dto.MoviDTO;
import co.com.comfaboy.herramietas.dbintegra.dto.ReciboDirectoDTO;
import co.com.comfaboy.herramietas.dbintegra.dto.TransaccionDTO;

/**
 * Controlador de cargue de movimientos
 * @author emedina
 *
 */

@ManagedBean(name="ipsmovi")
@SessionScoped
public class MovimientosControl implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static final int MOVI_IPS=1;
	private static final int MOVI_EPS=2;
	private static final int MOVI_NEW_HOTEL=3;
	private static final int MOVI_SCHOOL_PACK = 4;
	private static final int MOVI_SPORT = 5;
	private static final int MOVI_CAMPUS = 6;
	
	private static final String NOMBRE_ARCHIVO_IPS="MIP";
	private static final String NOMBRE_ARCHIVO_NH="MNH";
	private static final String NOMBRE_ARCHIVO_NH1="MN1";
	private static final String NOMBRE_ARCHIVO_IPS1="MII";
	
	private String user;
	private Properties prop;
	private UploadedFile fileMovIps; 
	private UploadedFile fileMovEps;
	private UploadedFile fileMovNehot;
	private List<String>errores;
	private List<String>datosCarga;
	private List<String>indices;
	private List<EstructuraArchMoviDTO> estruturaArchivoDTOs;
	private List<EstructuraArchMoviDTO> estruturaArchiTemp;
	private List<Tinmcxc> listTinmcxc;
	private List<Tinmreca> tinmrecas;
	private List<ConsigancionLineaDTO> tinmcolis;
	private List<Tinmsede> listSede;
	private List<String> comprobantes;
	private List<Tinmcxp> listTinmcxp;
	private List<ConsignacionDirectaDTO> tinmcodis;
	private List<ReciboDirectoDTO> tinmrecads;
	private List<FacturasDTO> facturasDTOs; 
	private boolean showErrores;
	private boolean showErroresMovi;
	private boolean showErroresNh;
	private boolean processMovi;
	private Tinpmcont tinpmcont;
	private Tinpreca tinpreca;
	private Tinpcoli tinpcoli;
	private Tinpcxc tinpcxc;
	private Tinpcxp tinpcxp;
	private Tinpcodi tinpcodi;
	private Tinprecad tinprecad;
	private Tinpfact tinpfact;
	private TransaccionDTO transaccion;
	private Map<String, TransaccionDTO> transaccionMap;
	private boolean dialConfM;
	private boolean dialConfMEps;
	private boolean dialConfMCampus;
	private boolean dialConfNh;
	private boolean dialConfFecNh;
	private boolean dialConfFecSchool;
	private boolean dialConfSport;
	private boolean btnMovIPS;
	private boolean btnMovIPSConf;
	private boolean btnMovNh;
	private boolean btnMovNhConf;
	private boolean btnMovEPS;
	private boolean btnMovEPSConf;
	private boolean btnMovCampus;
	private boolean btnMovCampusConf;
	private boolean btnMovSchool;
	private boolean btnMovSchoolConf;
	private boolean btnMovSport;
	private boolean btnMovSportConf;
	private List<String> erroresMovi; 
	private Date fecIni;
	private Date fecFin;
	private Date fecIniSP;
	private Date fecFinSP;
	private Date fecIniSt;
	private Date fecFinSt;
	private Date fecIniC;
	private Date fecFinC;
	private Integer sede;
	private Integer sedeEps;
	private Integer sedeNh;
	private Integer sedeSP;
	private Integer sedeS;
	private Integer sedeC;
	private Date fechaMov;
	private String verificacion;
	private String verificacionDos;
	private Integer tipoMovIPS;
	
	
	@EJB
	IntegracionServicio integracionServicio;
	
	@EJB
	HtasServicio htasServicio;
	
	@EJB
	FinanArsServicio arsServicio;
	
	@EJB
	FinanContService contService;

	public MovimientosControl() {
		try{
			errores = new ArrayList<String>();
			datosCarga = new ArrayList<String>();
			estruturaArchivoDTOs = new ArrayList<EstructuraArchMoviDTO>();
			showErrores = false;
			processMovi = false;
			dialConfM = false;
			dialConfMEps = false;
			dialConfNh = false;
			dialConfFecNh = false;
			dialConfMCampus = false;
			dialConfFecSchool = false;
			dialConfSport = false;
			btnMovIPS = false;
			btnMovIPSConf = true;
			btnMovEPS = false;
			btnMovEPSConf = true;
			btnMovCampus = false;
			btnMovCampusConf = true;
			btnMovSchool = false;
			btnMovSchoolConf=true;
			btnMovSport = false;
			btnMovSportConf=true;
			btnMovNh= false;
			btnMovNhConf = true;
			showErroresMovi = false;
			showErroresNh = false;
			tinpreca = new Tinpreca();
			tinpmcont = new Tinpmcont();
			prop=new Properties();
			transaccion = new TransaccionDTO();
			indices = new ArrayList<String>();
			errores = new ArrayList<String>();
			erroresMovi = new ArrayList<String>();
			listSede = new ArrayList<Tinmsede>();
			tinmrecas = new ArrayList<Tinmreca>();
			tinpcoli = new Tinpcoli();
			tinpcodi = new Tinpcodi();
			tinprecad = new Tinprecad();
			tinmcolis = new ArrayList<ConsigancionLineaDTO>();
			listTinmcxp = new ArrayList<Tinmcxp>();
			tinmcodis = new ArrayList<ConsignacionDirectaDTO>();
			tinmrecads = new ArrayList<ReciboDirectoDTO>();
			estruturaArchiTemp = new ArrayList<EstructuraArchMoviDTO>();
			facturasDTOs = new ArrayList<FacturasDTO>();
			listTinmcxc = new ArrayList<>();
			fechaMov = null;
			tinpcxc = new Tinpcxc();
			tinpcxp = new Tinpcxp();
			comprobantes = new ArrayList<String>();
			prop.load(MovimientosControl.class.getResourceAsStream("ldap2.properties"));
			prop.load(MovimientosControl.class.getResourceAsStream("mensajes.properties"));
			user=(String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(LoginControl.AUTH_KEY);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@PostConstruct
	public void inicializa(){
		try {
			listSede = integracionServicio.traersedes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * metodos de cargue de archivos
	 * @author emedina
	 */
	public void handleFileUploadMoviIps(FileUploadEvent event) {
		fileMovIps=event.getFile();
	}
	
	public void handleFileUploadMoviEps(FileUploadEvent event) {
		fileMovEps=event.getFile();
	}
	
	public void handleFileUploadMoviNewHot(FileUploadEvent event) {
		fileMovNehot=event.getFile();
	}
	
	/**
	 * metodo que escuha el boton de cargar archivo y realiza la validacion del 
	 * archivo para su cargue IPS
	 */
	public void cargarArchivoMoviIps(){
		try {
			
			if(fileMovIps!=null && sede>0 && tipoMovIPS>0){
				if(validarEstructura(MOVI_IPS, fileMovIps.getInputstream())){
					tinpmcont = integracionServicio.consultarTipoDeModalidadContable(2, sede);
					tinpreca = integracionServicio.consultarTipoReciboCaja(2, sede);
					tinpcxc = integracionServicio.consultarTipoCxC(sede,2);
					btnMovIPSConf = false;
					btnMovIPS = true;
				}
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorArch")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorprocess")));
		}
		
	}
	
	/**
	 * metodo que escuha el boton de cargar archivo y realiza la validacion del 
	 * archivo para su cargue de EPS
	 */
	public void cargarMoviEps(){
		try {
				if((fecIni!=null && !fecIni.equals("")) && (fecFin!=null && !fecFin.equals("")) && sedeEps>0){
					if(validarEstructura(MOVI_EPS, null)){
						tinpmcont = integracionServicio.consultarTipoDeModalidadContable(3, sedeEps);
						btnMovEPSConf = false;
						btnMovEPS = true;
						FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_INFO,"", prop.getProperty("exitoProcess")));
					}
				}else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorArch")));
				}
				
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorprocess")));
		}
		
	}
	
	/**
	 * metodo que escuha el boton de cargar archivo y realiza la validacion del 
	 * archivo para su cargue IPS
	 */
	public void cargarArchivoMoviNewHotel(){
		try {
			if(fileMovNehot!=null && sedeNh>0){
				tinpmcont = integracionServicio.consultarTipoDeModalidadContable(6, sedeNh);
				if(validarEstructura(MOVI_NEW_HOTEL, fileMovNehot.getInputstream())){
					tinpcodi = integracionServicio.consultarTipoDeConsignacionDirecta(sedeNh,6);
					tinprecad = integracionServicio.consultarTipoReciboCajaDirecto(sedeNh,6);
					tinpcxc = integracionServicio.consultarTipoCxC(sedeNh,6);
					tinpcxp = integracionServicio.consultarTipoCxP(sedeNh,6);
					tinpreca = integracionServicio.consultarTipoReciboCaja(6, sedeNh);
					tinpcoli = integracionServicio.consultarTipoCOnsignacion(6, sedeNh);
					btnMovNhConf = false;
					btnMovNh = true;
					FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_INFO,"", prop.getProperty("exitoProcess")));
				}
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorArch")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorprocess")));
		}
		
	}
	
	/**
	 * metodo que trae los datos de movimientos de school pack
	 */
	public void cargarMoviSchoolPack(){
		try {
			if((fecIniSP!=null && !fecIniSP.equals("")) && (fecFinSP!=null && !fecFinSP.equals("")) && sedeSP>0){
				comprobantes = integracionServicio.consultarComprobants(sedeSP, 7);
				if(validarEstructura(MOVI_SCHOOL_PACK, null)){
					tinpmcont = integracionServicio.consultarTipoDeModalidadContable(7, sedeSP);
					tinpreca = integracionServicio.consultarTipoReciboCaja(7, sedeSP);
					tinpcxc = integracionServicio.consultarTipoCxC(sedeSP,7);
					tinpcoli = integracionServicio.consultarTipoCOnsignacion(7, sedeSP);
					tinpcodi = integracionServicio.consultarTipoDeConsignacionDirecta(sedeSP,7);
					tinpfact = integracionServicio.consultarTipoFactura(7, sedeSP);
					btnMovSchool = true;
					btnMovSchoolConf = false;
					FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_INFO,"", prop.getProperty("exitoProcess")));
				}
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorArch")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorprocess")));
		}
	}
	
	/**
	 *  metodo que trae los datos de movimientos de sport
	 */
	public void cargarMoviSport(){
		try{
			if((fecIniSt!=null && !fecIniSt.equals("")) && (fecFinSt!=null && !fecFinSt.equals("")) && sedeS>0){
				comprobantes = integracionServicio.consultarComprobants(sedeS, 9);
				if(validarEstructura(MOVI_SPORT, null)){
					tinpmcont = integracionServicio.consultarTipoDeModalidadContable(9, sedeS);
					tinpreca = integracionServicio.consultarTipoReciboCaja(9, sedeS);
					tinpcxc = integracionServicio.consultarTipoCxC(sedeS,9);
					tinpcoli = integracionServicio.consultarTipoCOnsignacion(9, sedeS);
					tinpcodi = integracionServicio.consultarTipoDeConsignacionDirecta(sedeS,9);
					tinpfact = integracionServicio.consultarTipoFactura(9, sedeS);
					btnMovSport = true;
					btnMovSportConf = false;
					FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_INFO,"", prop.getProperty("exitoProcess")));
				}
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorArch")));
			}
		}catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorprocess")));
		}
	}
	
	/**
	 *  metodo que trae los datos de movimientos de Campus
	 */
	public void cargarMoviCampus(){
		try{
			if((fecIniC!=null && !fecIniC.equals("")) && (fecFinC!=null && !fecFinC.equals("")) && sedeC>0){
				comprobantes = integracionServicio.consultarComprobants(sedeC, 8);
				if(validarEstructura(MOVI_CAMPUS, null)){
					tinpmcont = integracionServicio.consultarTipoDeModalidadContable(8, sedeC);
					tinpreca = integracionServicio.consultarTipoReciboCaja(8, sedeC);
					tinpcxc = integracionServicio.consultarTipoCxC(sedeC,8);
					tinpcoli = integracionServicio.consultarTipoCOnsignacion(8, sedeC);
					tinpcodi = integracionServicio.consultarTipoDeConsignacionDirecta(sedeC,8);
					tinpfact = integracionServicio.consultarTipoFactura(8, sedeC);
					btnMovCampusConf = false;
					btnMovCampus = true;
					FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_INFO,"", prop.getProperty("exitoProcess")));
				}
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorArch")));
			}
		}catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorprocess")));
		}
	}
		
	/**
	 * metodo que llama a la logica de validacion de estructura de archivos
	 * @param opcion
	 * @param file
	 * @return
	 * @author emedina
	 */
	private boolean validarEstructura(int opcion, InputStream file){
		boolean valida= false;
		errores.clear();
		erroresMovi.clear();
		datosCarga.clear();
		int count =0;
		String datos[] = null;
		switch (opcion) {
		case MOVI_IPS:
			try {
				transaccionMap = new HashMap<String, TransaccionDTO>();
				String nombreArchivoIPs = null;
				switch (tipoMovIPS) {
					case 1:
						nombreArchivoIPs = NOMBRE_ARCHIVO_IPS;
						break;
					case 2:
						nombreArchivoIPs = NOMBRE_ARCHIVO_IPS1;
						break;

				}
				datosCarga= htasServicio.validaArchs(file, nombreArchivoIPs, 2,false);
				String aux=datosCarga.get(0);
				if(aux.substring(0,5).equalsIgnoreCase("error")){
					erroresMovi=datosCarga;
					valida = false;
					showErroresMovi = true;
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("validaTuterr")));
				}else{
					boolean movBien= false;
					for(int i=0; i<datosCarga.size();){
						datos = datosCarga.get(0).split(",");
						if(validarArchivoIPSSede(datos[0])){
							movBien = true;
							break;
						}else{
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorArchSede")));
							movBien = false;
							break;
						}
					}
					
					if(movBien){
						for(String dat: datosCarga){
							EstructuraArchMoviDTO archivoDTO = new EstructuraArchMoviDTO();
							datos = dat.split(",");
							count = count++;
							if (datos[4].matches("[+-]?\\d*(\\.\\d+)?")) {
								archivoDTO.setCamp1(datos[0]);
								archivoDTO.setCamp4(datos[1]);
								Date fecha = new Date();
								SimpleDateFormat forma = new SimpleDateFormat("dd/MM/yyyy");
								fecha = forma.parse(datos[2]);
								archivoDTO.setCamp2(fecha);
								archivoDTO.setCamp5(datos[3]);
								archivoDTO.setCamp6(datos[4]);
								archivoDTO.setCamp7(datos[5]);
								archivoDTO.setCamp8(datos[6]);
								archivoDTO.setCamp9(datos[7]);
								archivoDTO.setCamp10(datos[8]);
								archivoDTO.setCamp11(datos[9]);
								archivoDTO.setCamp12(datos[10]);
								archivoDTO.setCamp13(datos[11]);
								archivoDTO.setCamp14(datos[12]);
								archivoDTO.setCamp15(datos[13]);
								Date fecha2 = new Date();
								fecha2 = forma.parse(datos[14]);
								archivoDTO.setCamp3(fecha2);
								archivoDTO.setCamp16(datos[15]);
								archivoDTO.setCamp17(datos[16]);
								Date fecha3 = new Date();
								fecha3 = forma.parse(datos[17]);
								archivoDTO.setCamp21(fecha3);
								archivoDTO.setCamp18(datos[18]);
								archivoDTO.setCamp19(datos[19]);

								MoviDTO moviDTO = new MoviDTO();
								moviDTO.setComprobante(archivoDTO.getCamp1());
								moviDTO.setNumero(Integer.valueOf(archivoDTO.getCamp4()));
								moviDTO.setDescripcion(archivoDTO.getCamp10());
								moviDTO.setFecha(archivoDTO.getCamp2());
								moviDTO.setfVence(archivoDTO.getCamp21());
								moviDTO.setDebCre(archivoDTO.getCamp9());
								moviDTO.setValor(new BigDecimal(archivoDTO.getCamp8()));
								moviDTO.setNumeroDoc(archivoDTO.getCamp6());

								String moviclave = archivoDTO.getCamp1().concat(archivoDTO.getCamp4());

								if (transaccionMap.get(moviclave) == null) {
									transaccion = asignarTransaccion(moviDTO);
									transaccionMap.put(moviclave, transaccion);
									indices.add(archivoDTO.getCamp1() + archivoDTO.getCamp4());
								}

								if (moviDTO.getDebCre().compareTo("D") == 0) {
									transaccionMap.get(moviDTO.getComprobante() + moviDTO.getNumero()).addMovimientoDebito(moviDTO);
								} else if (moviDTO.getDebCre().compareTo("C") == 0) {
									transaccionMap.get(moviDTO.getComprobante() + moviDTO.getNumero()).addMovimientoCredito(moviDTO);
								}

								estruturaArchiTemp.add(archivoDTO);
							} else {
								erroresMovi.add("EL DOCUMENTO TIENE CARACTERES NO NUMERICOS " + datos[4]+ " EN LA LINEA " + (count + 1));
							}
						}
						
						if(erroresMovi.size()>0){
							valida = false;
							showErroresMovi = true;
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("validaTuterr")));
						}else{
							if (validarMovimientos()) {
								valida = true;
								estruturaArchivoDTOs = estruturaArchiTemp;
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", prop.getProperty("exitoProcess")));
							} else {
								valida = false;
								estruturaArchivoDTOs.clear();
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", prop.getProperty("erroCuentaBalanceo")));
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_ERROR,"", prop.getProperty("errorProcesArchivo")));
			}
			break;
		case MOVI_EPS:
				try {
					estruturaArchivoDTOs.clear();
					List<EpsDto> moviDTOs = new ArrayList<EpsDto>();
					moviDTOs = contService.consultarMovimientosArs(fecIni, fecFin);
					SimpleDateFormat formart = new SimpleDateFormat("dd/MM/yyyy");
					if(moviDTOs.size()>0){
						for(EpsDto dto: moviDTOs){
							EstructuraArchMoviDTO archivoDTO = new EstructuraArchMoviDTO();
							archivoDTO.setCamp1(dto.getComprobante());
							String fecha =dto.getDia()+"/"+dto.getMes()+"/"+dto.getAnyo();
							Date fh = formart.parse(fecha);
							archivoDTO.setCamp2(fh);
							archivoDTO.setCamp9(dto.getTipo());
							archivoDTO.setCamp4(String.valueOf(dto.getNumero()));
							archivoDTO.setCamp7(dto.getCentro());
							archivoDTO.setCamp10(dto.getDescripcion());
							archivoDTO.setCamp11(dto.getComprobante());
							archivoDTO.setCamp5(dto.getCuenta());
							archivoDTO.setCamp8(dto.getValor().toString());
							archivoDTO.setCamp6(String.valueOf(dto.getNit()));
							estruturaArchivoDTOs.add(archivoDTO);
						}
						valida = true;
					}else{
						FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_ERROR,"", prop.getProperty("errorDatos")));
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_ERROR,"", prop.getProperty("errorConsMovi")));
				}
				
			break;
		case MOVI_NEW_HOTEL:
			try {
				String nomArch = null;
				String tipoArchi = fileMovNehot.getFileName().substring(0,3);
				switch (tipoArchi) {
					case "IMH":
						nomArch = NOMBRE_ARCHIVO_NH;
						break;
					case "IMK":
						nomArch = NOMBRE_ARCHIVO_NH1;
						break;
					case "IPH":
						nomArch = NOMBRE_ARCHIVO_NH;
						break;
					case "IPK":
						nomArch = NOMBRE_ARCHIVO_NH1;
						break;
					case "IRH":
						nomArch = NOMBRE_ARCHIVO_NH;
						break;
					case "IRK":
						nomArch = NOMBRE_ARCHIVO_NH1;
						break;
				}
				
				datosCarga= htasServicio.validaArchs(file, nomArch, 2,true);
				String aux=datosCarga.get(0);
				if(aux.substring(0,5).equalsIgnoreCase("error")){
					errores=datosCarga;
					valida = false;
					showErroresNh = true;
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("validaTuterr")));
				}else{
					for(String dat: datosCarga){
						EstructuraArchMoviDTO archivoDTO = new EstructuraArchMoviDTO();
						datos = dat.split(",");
						count = count++;
//						if(datos[5].replace("^\\s*","").trim().matches("[+-]?\\d*(\\.\\d+)?")){
							System.out.println("HERRAMIENTAS-->dato cargado: "+datos[8]+datos[9]);
							archivoDTO.setCamp1(fileMovNehot.getFileName().substring(0,3));
							archivoDTO.setCamp4(String.valueOf(validaDatoReferncia(datos[9].replace("^\\s*","").trim())));
							Date fecha = new Date();
							SimpleDateFormat forma = new SimpleDateFormat("dd/MM/yyyy");
							fecha = forma.parse(datos[1]);
							archivoDTO.setCamp2(fecha);
							archivoDTO.setCamp7(datos[2].replace("^\\s*","").trim());
							archivoDTO.setCamp5(datos[4].replace("^\\s*","").trim());
							archivoDTO.setCamp6(!datos[5].replace("^\\s*","").trim().equals("0") ? datos[5].replace("^\\s*","").trim():tinpmcont.getPmmcontNit());
							archivoDTO.setCamp8(datos[6].replace("^\\s*","").trim());
							archivoDTO.setCamp9(datos[7].replace("^\\s*","").trim());
							archivoDTO.setCamp10(datos[8].replace("^\\s*","").trim().concat(tipoArchi));
							archivoDTO.setCamp12(datos[9].replace("^\\s*","").trim());
							archivoDTO.setCamp11(tipoArchi);
							Date fecha2 = new Date();
							fecha2 = forma.parse(datos[11].replace("^\\s*","").trim());
							archivoDTO.setCamp3(fecha2);
							Date fecha3 = new Date();
							fecha3 = forma.parse(datos[11].replace("^\\s*","").trim());
							archivoDTO.setCamp21(fecha3);
							archivoDTO.setCamp20(tipoArchi);
							
							switch (tipoArchi) {
							case "IMH":
								archivoDTO.setCamp13(datos[10].replace("^\\s*","").trim());
								break;
							case "IMK":
								archivoDTO.setCamp13(datos[10].replace("^\\s*","").trim().substring(0,1).equals("0")?"0":datos[10].replace("^\\s*","").trim());
								break;
							case "IPH":
								archivoDTO.setCamp13(datos[10].replace("^\\s*","").trim());
								break;
							case "IPK":
								archivoDTO.setCamp13(datos[10].replace("^\\s*","").trim().substring(0,1).equals("0")?"0":datos[10].replace("^\\s*","").trim());
								break;
							case "IRH":
								archivoDTO.setCamp13(datos[10].replace("^\\s*","").trim());
								break;
							case "IRK":
								archivoDTO.setCamp13(datos[10].replace("^\\s*","").trim().substring(0,1).equals("0")?"0":datos[10].replace("^\\s*","").trim());
								break;
						}
							
							estruturaArchivoDTOs.add(archivoDTO);
							fechaMov = fecha;
//						}else{
//							erroresMovi.add("EL DOCUMENTO TIENE CARACTERES NO NUMERICOS "+ datos[5] +" EN LA LINEA " +(count+1));
//						}
						
					}
					
					if(erroresMovi.size()>0){
						valida = false;
						showErroresMovi = true;
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("validaTuterr")));
					}else{
						valida = true;
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_ERROR,"", prop.getProperty("errorProcesArchivo")));
			}
			break;
		case MOVI_SCHOOL_PACK:
			try {
				estruturaArchivoDTOs.clear();
				List<MoviDTO> moviDTOs = new ArrayList<MoviDTO>();
				moviDTOs = contService.consultarMovimientosFinanCont(comprobantes, fecIniSP, fecFinSP, MOVI_SCHOOL_PACK);
				if(moviDTOs.size()>0){
					for(MoviDTO dto: moviDTOs){
						EstructuraArchMoviDTO archivoDTO = new EstructuraArchMoviDTO();
						archivoDTO.setCamp1(dto.getComprobante());
						archivoDTO.setCamp4(String.valueOf(dto.getNumero()));
						archivoDTO.setCamp2(dto.getFecha());
						archivoDTO.setCamp5(dto.getCuentaBean());
						archivoDTO.setCamp6(dto.getNitBean());
						archivoDTO.setCamp7(dto.getCentroBean());
						archivoDTO.setCamp8(dto.getValor().toString());
						archivoDTO.setCamp9(dto.getDebCre());
//						String descrip = null;
//						if(sedeSP==8 || sedeSP==9){
//							descrip = "SALUD LEY 21";
//						}else{
//							descrip = "EDUCACION";
//						}
						archivoDTO.setCamp10(dto.getDescripcion());
						archivoDTO.setCamp11(dto.getTipoDoc());
						archivoDTO.setCamp12(dto.getNumeroDoc());
						archivoDTO.setCamp21(dto.getFecha());
						estruturaArchivoDTOs.add(archivoDTO);
					}
					valida = true;
				}else{
					FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_ERROR,"", prop.getProperty("errorDatos")));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_ERROR,"", prop.getProperty("errorConsMovi")));
			}
			break;
		case MOVI_SPORT:
			try {
				estruturaArchivoDTOs.clear();
				List<MoviDTO> moviDTOs = new ArrayList<MoviDTO>();
				moviDTOs = contService.consultarMovimientosFinanCont(comprobantes, fecIniSt, fecFinSt, MOVI_SPORT);
				if(moviDTOs.size()>0){
					for(MoviDTO dto: moviDTOs){
						EstructuraArchMoviDTO archivoDTO = new EstructuraArchMoviDTO();
						archivoDTO.setCamp1(dto.getComprobante());
						archivoDTO.setCamp4(String.valueOf(dto.getNumero()));
						archivoDTO.setCamp2(dto.getFecha());
						archivoDTO.setCamp5(dto.getCuentaBean());
						archivoDTO.setCamp6(dto.getNitBean());
						archivoDTO.setCamp7(dto.getCentroBean());
						archivoDTO.setCamp8(dto.getValor().toString());
						archivoDTO.setCamp9(dto.getDebCre());
//						archivoDTO.setCamp10("RECREACION DEPORTE Y TURISMO");
						archivoDTO.setCamp10(dto.getDescripcion());
						archivoDTO.setCamp11(dto.getTipoDoc());
						archivoDTO.setCamp12(dto.getNumeroDoc());
						archivoDTO.setCamp21(dto.getFecha());
						estruturaArchivoDTOs.add(archivoDTO);
					}
					valida = true;
				}else{
					FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_ERROR,"", prop.getProperty("errorDatos")));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_ERROR,"", prop.getProperty("errorConsMovi")));
			}
			break;
		case MOVI_CAMPUS:
			try {
				estruturaArchivoDTOs.clear();
				List<MoviDTO> moviDTOs = new ArrayList<MoviDTO>();
				moviDTOs = contService.consultarMovimientosFinanCont(comprobantes, fecIniC, fecFinC,MOVI_CAMPUS);
				if(moviDTOs.size()>0){
					for(MoviDTO dto: moviDTOs){
						EstructuraArchMoviDTO archivoDTO = new EstructuraArchMoviDTO();
						archivoDTO.setCamp1(dto.getComprobante());
						archivoDTO.setCamp4(String.valueOf(dto.getNumero()));
						archivoDTO.setCamp2(dto.getFecha());
						archivoDTO.setCamp5(dto.getCuentaBean());
						archivoDTO.setCamp6(dto.getNitBean());
						archivoDTO.setCamp7(dto.getCentroBean());
						archivoDTO.setCamp8(dto.getValor().toString());
						archivoDTO.setCamp9(dto.getDebCre());
//						archivoDTO.setCamp10("EDUCACION PARA EL TRABAJO Y DESARROLLO H");
						archivoDTO.setCamp10(dto.getDescripcion());
						archivoDTO.setCamp11(dto.getTipoDoc());
						archivoDTO.setCamp12(dto.getNumeroDoc());
						archivoDTO.setCamp21(dto.getFecha());
						estruturaArchivoDTOs.add(archivoDTO);
					}
					valida = true;
				}else{
					FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_ERROR,"", prop.getProperty("errorDatos")));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_ERROR,"", prop.getProperty("errorConsMovi")));
			}
			break;
		}
		return valida;
	}
	
	
	/**
	 * 
	 */
	private Boolean validarMovimientos(){
		boolean ok = false;
		TransaccionDTO transaccionRealizar=new TransaccionDTO();
		TransaccionDTO transaccionTemp=new TransaccionDTO();
		List<TransaccionDTO> listok= new ArrayList<TransaccionDTO>();
		for(String cadena: indices){
			transaccionTemp =transaccionMap.get(cadena);
			transaccionRealizar = integracionServicio.iniciarBalanceo(transaccionTemp);
			listok.add(transaccionRealizar);
		}
		
		if(listok.size()>0){
			ok = true;
		}
		
		return ok;
	}
	
	/**
	 * 
	 * @param movi
	 * @return
	 */
	public TransaccionDTO asignarTransaccion(MoviDTO movi) {

		TransaccionDTO transaccionGuardar = new TransaccionDTO();
		 
		if (movi != null && !movi.equals("")) {
			transaccionGuardar.setComprobante(movi.getComprobante());
			transaccionGuardar.setDescripcionTransaccion(movi.getDescripcion());
			transaccionGuardar.setFechaTransaccion(movi.getFecha());
			transaccionGuardar.setFechaVencimientoTransaccion(movi.getfVence());
		}
		return transaccionGuardar;
	}
	
	
	private boolean validarCero(String valor){
		boolean reton = false;
		
		if(valor.equals("0.00")){
			reton = false;
		}else if(valor.equals("0")){
			reton = false;
		}else if(valor.equals("-0.00")){
		}else{
			reton = true;
		}
		
		return reton;
	}
	
	
	/**
	 * metodo que almacena el movimiento contable en la base intermedia
	 * @author emedina
	 */
	public void guardarMovimiento(Long opcion){
		boolean exito = false;
		try {
			int lin=1;
			int cont =0;
			int contRec = 0;
			String moviComp = null;
			Tinmrecad recTmp = new Tinmrecad();
			erroresMovi.clear();
			errores.clear();
			if(!processMovi){
				synchronized (MovimientosControl.class) {
					processMovi = true;
					List<Tinmmcont> tinmmconts = new ArrayList<Tinmmcont>();
					List<Tinddemc> tinddemcs = new ArrayList<Tinddemc>();
					listTinmcxc = new ArrayList<Tinmcxc>();
					listTinmcxp = new ArrayList<Tinmcxp>();
					tinmcodis = new ArrayList<ConsignacionDirectaDTO>();
					tinmrecads = new ArrayList<ReciboDirectoDTO>();
					Tinmmcont tinmmcont = new Tinmmcont();
					Tinddemc tinddemc = new Tinddemc();
					Tinmarbol tinmarbol = new Tinmarbol();
					Tinmcuen tinmcuen = new Tinmcuen();
					int prog =0;
					int sd = 0;
					String cubNume = null;
					if(estruturaArchivoDTOs.size()>0){
						for(EstructuraArchMoviDTO archMoviDTO:estruturaArchivoDTOs){
							boolean recibo = false;
							
							switch (opcion.intValue()) {
							case MOVI_IPS:
								prog =2;
								sd = sede;
								break;
							case MOVI_EPS:
								prog =3;
								sd = sedeEps;
								break;
							case MOVI_NEW_HOTEL:
								prog =6;
								sd = sedeNh;
								break;
							case MOVI_SCHOOL_PACK:
								prog =7;
								sd = sedeSP;
								break;
							case MOVI_SPORT:
								prog =9;
								sd = sedeS;
								break;
							case MOVI_CAMPUS:
								prog =8;
								sd = sedeC;
								break;
						}
							
							SimpleDateFormat dfr = new SimpleDateFormat("dd/MM/yyyy");
							if(opcion.intValue()!=MOVI_EPS){
								String fecRCD = dfr.format(archMoviDTO.getCamp2());
								recTmp = integracionServicio.consultarReciboDeCajaDirectoExistente(fecRCD, sd, prog, archMoviDTO.getCamp20());
							}
							
							if(validarCero(archMoviDTO.getCamp8())){
//								System.out.println("DOCUMENTOS FALLIDO->"+archMoviDTO.getCamp10());
								if(validarReciboCaja((archMoviDTO.getCamp1()))){
									SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
									String fecha = df.format(archMoviDTO.getCamp2());
									
									String fechaMte = integracionServicio.consultarReciboDeCajaExistente(Integer.parseInt(fecha),Integer.parseInt(archMoviDTO.getCamp4()), opcion.intValue(),archMoviDTO.getCamp20()).getRecaMteFech(); 
									if(fechaMte!=null && !fechaMte.equals("")){
										recibo = true;
									}
								}else if(recTmp.getRecadMteFech()!=null){
										recibo = true;
								}else{
									if(opcion.intValue()==MOVI_NEW_HOTEL){
										if(integracionServicio.consultarMovimientoNh(prog, archMoviDTO.getCamp11().trim(), sedeNh, archMoviDTO.getCamp2())>0){
											recibo = true;
										}
									}else{
										if(integracionServicio.consultarMovimiento(Integer.valueOf(archMoviDTO.getCamp4()), archMoviDTO.getCamp1(),archMoviDTO.getCamp2()).getMcontSerial()>0){
											recibo=true;
										}
									}
									
									
								}
								
							if(recibo){
									if(opcion.intValue()==MOVI_IPS){
										erroresMovi.add(prop.getProperty("errorLinea").concat(archMoviDTO.getCamp1().concat("-").concat(archMoviDTO.getCamp4().concat(",").concat("linea "+lin))));
										showErroresMovi = true;
									}else if(opcion.intValue()==MOVI_NEW_HOTEL){
										errores.add(prop.getProperty("errorLinea").concat(archMoviDTO.getCamp1().concat("-").concat(archMoviDTO.getCamp4().concat(",").concat("linea "+lin))));
										showErroresNh = true;
									}
								}else{
									if(validarNoEsReciboCaja(archMoviDTO.getCamp1(),opcion.intValue())){
										tinmmcont = new Tinmmcont();
										tinddemc = new Tinddemc();
										tinmarbol = new Tinmarbol();
										tinmcuen = new Tinmcuen();
										tinmmcont.setMcontEmpCodi(tinpmcont.getPmcontEmpCodi());
										tinmmcont.setMcontArbCsuc(tinpmcont.getPmcontArbCsuc());
										tinmmcont.setMcontEstado("L");
										tinmmcont.setMcontFCargue(new Date());
										tinmmcont.setMcontMcoCont(0);
										tinmmcont.setMcontMcoDesc(opcion.intValue()!=MOVI_NEW_HOTEL ? archMoviDTO.getCamp10():"MOVIMIENTO DE NEWHOTEL "+archMoviDTO.getCamp10().subSequence(archMoviDTO.getCamp10().length()-3, archMoviDTO.getCamp10().length()));
										tinmmcont.setMcontMcoFech(archMoviDTO.getCamp2());
										tinmmcont.setMcontMcoNume(opcion.intValue()==MOVI_NEW_HOTEL ? 0:Integer.valueOf(archMoviDTO.getCamp4())); // si es new hotel se envia un cero
										tinmmcont.setMcontMcoNumer(0);
										tinmmcont.setMcontModCodi((short)7);
										tinmmcont.setMcontProgSerial(tinpmcont.getPmcontProgSerial());
										tinmmcont.setMcontRetorno((short)0);
										tinmmcont.setMcontSedeSerial(tinpmcont.getPmcontSedeSerial());
										Short tiop = integracionServicio.tipoOperacion(archMoviDTO.getCamp1(),sd,prog);
										tinmmcont.setMcontTopCodi(tiop!=null && !tiop.equals("")?tiop:0);
										tinmmcont.setMcontTxerror("");
										tinmmconts.add(tinmmcont);
										
										try {
											tinmarbol = integracionServicio.consultarCentro(archMoviDTO.getCamp7());
											if(tinmarbol.getArbolSwebSerial()>0){
												tinddemc.setDemcArbCoda(tinmarbol.getArbolArea().trim());
												tinddemc.setDemcArbCodc(tinmarbol.getArbolCentro().trim());
												tinddemc.setDemcArbCodp(tinmarbol.getArbolProy().trim());
												tinddemc.setDemcArbCods(tinmarbol.getArbolSucu().trim());
											}else{
												FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorConstacentro").concat(" linea"+lin+" "+archMoviDTO.getCamp1().concat("-").concat(archMoviDTO.getCamp4()))));
											}
											
										} catch (Exception e) {
											FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorCentro").concat(" linea"+lin+" "+archMoviDTO.getCamp1().concat("-").concat(archMoviDTO.getCamp4()))));
										}
										
										try {	
											tinmcuen = integracionServicio.consltarCuenta(archMoviDTO.getCamp5(),sd,prog);
											if(tinmcuen.getCuentCodErp()!=null && !tinmcuen.getCuentCodErp().equals("")){
												tinddemc.setDemcCueCodi(tinmcuen.getCuentCodErp().toString());
												tinddemc.setDemcDmcActi(BigDecimal.ZERO);	
											}else{
												FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorCuenta").concat(" linea"+lin+" "+archMoviDTO.getCamp1().concat("-").concat(archMoviDTO.getCamp4()))));
											}
										} catch (Exception e) {
											FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorConCuenta").concat(" linea"+lin+" "+archMoviDTO.getCamp1().concat("-").concat(archMoviDTO.getCamp4()))));
										}
										
										tinddemc.setDemcDmcCant(BigDecimal.ZERO);
										tinddemc.setDemcDmcRefe(archMoviDTO.getCamp11());
										if(opcion.intValue()==MOVI_EPS){
											tinddemc.setDemcDmcVaba(tinmcuen.getCuenVaba().equals("S") ? BigDecimal.ONE : BigDecimal.ZERO);
										}else{
											tinddemc.setDemcDmcVaba(opcion==MOVI_NEW_HOTEL ? BigDecimal.valueOf(Double.valueOf(archMoviDTO.getCamp13())):BigDecimal.ZERO);
										}
										
										if(archMoviDTO.getCamp9().equals("C")){
											tinddemc.setDemcDmcVacr(BigDecimal.valueOf(Double.valueOf(archMoviDTO.getCamp8())));
										}else if(archMoviDTO.getCamp9().equals("D")){
											tinddemc.setDemcDmcVadb((BigDecimal.valueOf(Double.valueOf(archMoviDTO.getCamp8()))));
										}
										tinddemc.setDemcDmcDesc(opcion.intValue()!=MOVI_NEW_HOTEL ? archMoviDTO.getCamp10(): archMoviDTO.getCamp10().substring(0, archMoviDTO.getCamp10().length()-3));
										tinddemc.setDemcMcontSerial(tinmmcont.getMcontMcoNume());
										tinddemc.setDemcTerCoda(archMoviDTO.getCamp6());
										tinddemc.setDemcTerCodm("0");
											
										tinddemcs.add(tinddemc);
											
										//llamado al metodo de crear cuenta por cobrar
										if (opcion.intValue() != MOVI_EPS) {
											if(archMoviDTO.getCamp1().substring(0,2).equals("NC")){
												FacturasDTO facturasDTO = new  FacturasDTO();
												facturasDTO = crearFacturaCruce(archMoviDTO, opcion.intValue(), tinddemc); 
												if(facturasDTO != null){
													facturasDTOs.add(facturasDTO);
												}
											}else {
												Tinmcxc tinmcxcm = new Tinmcxc();
												tinmcxcm = crearCxC(tinddemc, archMoviDTO, opcion.intValue());
												if (tinmcxcm.getCxcCxcNume() > 0) {
													if (opcion == MOVI_NEW_HOTEL) {
														listTinmcxc.add(tinmcxcm);
													} else {
														if (cont != tinmmcont.getMcontMcoNume()) {
															listTinmcxc.add(tinmcxcm);
															cont = tinmmcont.getMcontMcoNume();
														}
													}
												}
											}

											if (opcion == MOVI_NEW_HOTEL) {
												Tinmcxp tinmcxp = new Tinmcxp();
												tinmcxp = crearCxp(tinddemc, archMoviDTO, opcion.intValue());
												if (tinmcxp.getMcxpPvdCoda() != null && !tinmcxp.getMcxpPvdCoda().equals("")) {
													listTinmcxp.add(tinmcxp);
												}

												ConsignacionDirectaDTO consignacionDirectaDTO = new ConsignacionDirectaDTO();
												consignacionDirectaDTO = crearConsignacionDirecta(archMoviDTO, opcion.intValue(), tinddemc);
												if (consignacionDirectaDTO != null) {
													tinmcodis.add(consignacionDirectaDTO);
												}

												ReciboDirectoDTO reciboDirectoDTO = new ReciboDirectoDTO();
												reciboDirectoDTO = crearReciboCajaDirecta(archMoviDTO, opcion.intValue(),tinddemc);
												if (reciboDirectoDTO != null) {
													tinmrecads.add(reciboDirectoDTO);
												}
											}
										}
									}else if(validarReciboCaja(archMoviDTO.getCamp1())){
										//llama al metodo de cargar el listado de recibos de caja
										Tinmreca rec = new Tinmreca();
										rec = crearReciboDeCaja(archMoviDTO,opcion.intValue(),sd,prog);
										if(contRec != rec.getRecaMteNume()){
											tinmrecas.add(rec);
											contRec = rec.getRecaMteNume();
										}
										
									}else if(validarConsignacionLinea(archMoviDTO.getCamp1(),opcion.intValue())){
										
										Tinmcuen cuen = integracionServicio.consltarCuenta(archMoviDTO.getCamp5(),sd,prog);
										
										if(archMoviDTO.getCamp9().equals("D") && cuen.getCuentDocum().equals("S") && cuen.getCuentSweb()==9){
											ConsigancionLineaDTO  lineaDTO = crearConsigancionEnLinea(archMoviDTO,opcion.intValue(), 9, cuen);
											if(lineaDTO!=null){
												tinmcolis.add(lineaDTO);
												moviComp = archMoviDTO.getCamp4();
												cubNume = cuen.getCuentCubNume();
											}else{
												moviComp = archMoviDTO.getCamp4();
												cubNume = cuen.getCuentCubNume();
											}
										}else if(archMoviDTO.getCamp9().equals("D") && cuen.getCuentDocum().equals("S") && cuen.getCuentSweb()==5){
											tinmmcont = new Tinmmcont();
											tinddemc = new Tinddemc();
											tinmarbol = new Tinmarbol();
											Tinmcxc tinmcxcm = new Tinmcxc();
											
											tinmmcont.setMcontEmpCodi(tinpmcont.getPmcontEmpCodi());
											tinmmcont.setMcontArbCsuc(tinpmcont.getPmcontArbCsuc());
											tinmmcont.setMcontEstado("L");
											tinmmcont.setMcontFCargue(new Date());
											tinmmcont.setMcontMcoCont(0);
											tinmmcont.setMcontMcoDesc(archMoviDTO.getCamp10());
											tinmmcont.setMcontMcoFech(archMoviDTO.getCamp2());
											tinmmcont.setMcontMcoNume(Integer.valueOf(archMoviDTO.getCamp4())); 
											tinmmcont.setMcontMcoNumer(0);
											tinmmcont.setMcontModCodi((short)7);
											tinmmcont.setMcontProgSerial(tinpmcont.getPmcontProgSerial());
											tinmmcont.setMcontRetorno((short)0);
											tinmmcont.setMcontSedeSerial(tinpmcont.getPmcontSedeSerial());
											tinmmcont.setMcontTopCodi(integracionServicio.tipoOperacion(archMoviDTO.getCamp1(),sd,prog));
											tinmmcont.setMcontTxerror("");
											tinmmconts.add(tinmmcont);
											try {
												tinmarbol = integracionServicio.consultarCentro(archMoviDTO.getCamp7());
												if(tinmarbol.getArbolSwebSerial()>0){
													tinddemc.setDemcArbCoda(tinmarbol.getArbolArea().trim());
													tinddemc.setDemcArbCodc(tinmarbol.getArbolCentro().trim());
													tinddemc.setDemcArbCodp(tinmarbol.getArbolProy().trim());
													tinddemc.setDemcArbCods(tinmarbol.getArbolSucu().trim());
													tinddemc.setDemcDmcDesc(archMoviDTO.getCamp10());
													tinddemc.setDemcDmcVacr(BigDecimal.valueOf(Double.valueOf(archMoviDTO.getCamp8())));
												}else{
													FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorConstacentro").concat(" linea"+lin+" "+archMoviDTO.getCamp1().concat("-").concat(archMoviDTO.getCamp4()))));
												}
													
											} catch (Exception e) {
												FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorCentro").concat(" linea"+lin+" "+archMoviDTO.getCamp1().concat("-").concat(archMoviDTO.getCamp4()))));
											}
												
											tinmcuen = integracionServicio.consltarCuenta(archMoviDTO.getCamp5(),sd,prog);
											try{
												if(tinmcuen.getCuentCodErp()!=null && !tinmcuen.getCuentCodErp().equals("")){
													tinddemc.setDemcCueCodi(tinmcuen.getCuentCodErp().toString());
													tinddemc.setDemcDmcActi(BigDecimal.ZERO);	
												}else{
													FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorCuenta").concat(" linea"+lin+" "+archMoviDTO.getCamp1().concat("-").concat(archMoviDTO.getCamp4()))));
												}
											} catch (Exception e) {
												FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorConCuenta").concat(" linea"+lin+" "+archMoviDTO.getCamp1().concat("-").concat(archMoviDTO.getCamp4()))));
											}
											
											tinddemc.setDemcDmcCant(BigDecimal.ZERO);
											tinddemc.setDemcDmcRefe(archMoviDTO.getCamp11());
											tinddemc.setDemcDmcVaba(BigDecimal.ONE);
											if(archMoviDTO.getCamp9().equals("D")){
												tinddemc.setDemcDmcVadb((BigDecimal.valueOf(Double.valueOf(archMoviDTO.getCamp8()))));
											}
											tinddemc.setDemcDmcDesc(archMoviDTO.getCamp10());
											tinddemc.setDemcMcontSerial(tinmmcont.getMcontMcoNume());
											tinddemc.setDemcTerCoda(archMoviDTO.getCamp6());
											tinddemc.setDemcTerCodm("1");
												
											tinddemcs.add(tinddemc);
											
											tinmcxcm = crearCxC(tinddemc, archMoviDTO, opcion.intValue());
											tinmcxcm.setCxcCxcTipo("F");
											moviComp = archMoviDTO.getCamp4();
										}else if(archMoviDTO.getCamp9().equals("C") && cuen.getCuentDocum().equals("S")){
											Tinmcxc tinmcxcm = new Tinmcxc();
											if(archMoviDTO.getCamp4().equals(moviComp)){
												if(cuen.getCuentSweb() == 9 || cuen.getCuentSweb()==5){
													if(cuen.getCuentSweb() == 9){
														cubNume = cuen.getCuentCubNume();
													}else if(cuen.getCuentSweb()==5){
														cubNume = cuen.getCuentCubNume().trim().equals("0") ? cubNume:cuen.getCuentCubNume();
														cuen.setCuentCubNume(cubNume);
													}
													
													ConsigancionLineaDTO lineaDTO = crearConsigancionEnLinea(archMoviDTO,opcion.intValue(), cuen.getCuentSweb(),cuen);
													if(lineaDTO != null){
														tinmcolis.add(lineaDTO);
														moviComp = archMoviDTO.getCamp4();
													}else{
														moviComp = archMoviDTO.getCamp4();
													}
												}if(cuen.getCuentSweb()==10){
													tinddemc = new Tinddemc();
													tinmarbol = new Tinmarbol();
													
													try {
														tinmarbol = integracionServicio.consultarCentro(archMoviDTO.getCamp7());
														if(tinmarbol.getArbolSwebSerial()>0){
															tinddemc.setDemcArbCoda(tinmarbol.getArbolArea().trim());
															tinddemc.setDemcArbCodc(tinmarbol.getArbolCentro().trim());
															tinddemc.setDemcArbCodp(tinmarbol.getArbolProy().trim());
															tinddemc.setDemcArbCods(tinmarbol.getArbolSucu().trim());
															tinddemc.setDemcDmcDesc(archMoviDTO.getCamp10());
															tinddemc.setDemcDmcVacr(BigDecimal.valueOf(Double.valueOf(archMoviDTO.getCamp8())));
														}else{
															FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorConstacentro").concat(" linea"+lin+" "+archMoviDTO.getCamp1().concat("-").concat(archMoviDTO.getCamp4()))));
														}
															
													} catch (Exception e) {
														FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorCentro").concat(" linea"+lin+" "+archMoviDTO.getCamp1().concat("-").concat(archMoviDTO.getCamp4()))));
													}
														
													tinmcuen = integracionServicio.consltarCuenta(archMoviDTO.getCamp5(),sd,prog);
													try{
														if(tinmcuen.getCuentCodErp()!=null && !tinmcuen.getCuentCodErp().equals("")){
															tinddemc.setDemcCueCodi(tinmcuen.getCuentCodErp().toString());
															tinddemc.setDemcDmcActi(BigDecimal.ZERO);	
														}else{
															FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorCuenta").concat(" linea"+lin+" "+archMoviDTO.getCamp1().concat("-").concat(archMoviDTO.getCamp4()))));
														}
													} catch (Exception e) {
														FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorConCuenta").concat(" linea"+lin+" "+archMoviDTO.getCamp1().concat("-").concat(archMoviDTO.getCamp4()))));
													}
													
													tinddemc.setDemcDmcCant(BigDecimal.ZERO);
													tinddemc.setDemcDmcRefe(archMoviDTO.getCamp11());
													tinddemc.setDemcDmcVaba(BigDecimal.ONE);
													if(archMoviDTO.getCamp9().equals("C")){
														tinddemc.setDemcDmcVacr(BigDecimal.valueOf(Double.valueOf(archMoviDTO.getCamp8())));
													}else if(archMoviDTO.getCamp9().equals("D")){
														tinddemc.setDemcDmcVadb((BigDecimal.valueOf(Double.valueOf(archMoviDTO.getCamp8()))));
													}
													tinddemc.setDemcDmcDesc(archMoviDTO.getCamp10());
													tinddemc.setDemcMcontSerial(tinmmcont.getMcontMcoNume());
													tinddemc.setDemcTerCoda(archMoviDTO.getCamp6());
													tinddemc.setDemcTerCodm("1");
													
													ConsignacionDirectaDTO directaDTO = crearConsignacionDirecta(archMoviDTO, opcion.intValue(), tinddemc);
													
													if(directaDTO!=null){
														tinmcodis.add(directaDTO);
													}
													moviComp = archMoviDTO.getCamp4();
												}
											}
											else{
												try {
													tinmarbol = integracionServicio.consultarCentro(archMoviDTO.getCamp7());
													if(tinmarbol.getArbolSwebSerial()>0){
														tinddemc.setDemcArbCoda(tinmarbol.getArbolArea().trim());
														tinddemc.setDemcArbCodc(tinmarbol.getArbolCentro().trim());
														tinddemc.setDemcArbCodp(tinmarbol.getArbolProy().trim());
														tinddemc.setDemcArbCods(tinmarbol.getArbolSucu().trim());
														tinddemc.setDemcDmcVacr(BigDecimal.valueOf(Double.valueOf(archMoviDTO.getCamp8())));
														tinddemc.setDemcDmcDesc(archMoviDTO.getCamp10());
													}else{
														FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorConstacentro").concat(" linea"+lin+" "+archMoviDTO.getCamp1().concat("-").concat(archMoviDTO.getCamp4()))));
													}
													
												} catch (Exception e) {
													FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorCentro").concat(" linea"+lin+" "+archMoviDTO.getCamp1().concat("-").concat(archMoviDTO.getCamp4()))));
												}
												tinmcxcm = crearCxC(tinddemc, archMoviDTO, opcion.intValue());
												tinmcxcm.setCxcCxcTipo("C");
											}
											moviComp = archMoviDTO.getCamp4();
										}
									}
								}
								lin++;
							}
						}
						if(erroresMovi.size()>0 || errores.size()>0){
							processMovi = false;
							dialConfM = false;
							btnMovIPS = false;
							btnMovIPSConf = true;
							btnMovNh =false;
							btnMovNhConf = true;
							fileMovIps = null;
							fileMovNehot = null;
							dialConfFecNh = false;
							btnMovCampusConf = true;
							btnMovCampus = false;
							dialConfMCampus= false;
							dialConfFecSchool = false;
							btnMovSchool = false;
							btnMovSchoolConf = true;
							dialConfSport = false;
							btnMovSport = false;
							btnMovSportConf = true;
							sede = 0;
							sedeC = 0;
							sedeEps=0;
							sedeNh=0;
							sedeS=0;
							sedeSP=0;
							tipoMovIPS=0;
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorExisMovi")));
						}else{
							
							if(tinmmconts.size()>0 && tinddemcs.size()>0){
								integracionServicio.guardarMovimiento(tinmmconts, tinddemcs);
								if(opcion!=2){
									if(listTinmcxc.size()>0){
										integracionServicio.guardarCxC(listTinmcxc);
										exito = true;
									}else{
										exito = true;
										FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"",prop.getProperty("errorCxc")));	
									}
								}
							}else{
								exito = true;
								dialConfM = false;
								btnMovIPS = false;
								btnMovIPSConf = true;
								btnMovNh =false;
								btnMovNhConf = true;
								fileMovIps = null;
								fileMovNehot = null;
								dialConfMEps = false;
								btnMovEPS = false;
								btnMovEPSConf = true;
								dialConfFecNh = false;
								btnMovCampusConf = true;
								btnMovCampus = false;
								dialConfMCampus= false;
								dialConfFecSchool = false;
								btnMovSchool = false;
								btnMovSchoolConf = true;
								dialConfSport = false;
								btnMovSport = false;
								btnMovSportConf = true;
								sede = 0;
								sedeC = 0;
								sedeEps=0;
								sedeNh=0;
								sedeS=0;
								sedeSP=0;
								tipoMovIPS=0;
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"",prop.getProperty("errormovi")));
							}
							
							if(exito){
								if(tinmrecas.size()>0){
									integracionServicio.guardarReciboDeCaja(tinmrecas);
									exito = true;
								}else{
									exito = true;
									FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"",prop.getProperty("errorRC")));	
								}
								
								if(tinmcolis.size()>0){
									integracionServicio.guardarConsigancionLinea(tinmcolis);
									exito = true;
								}else{
									exito = true;
									FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"",prop.getProperty("errorCoLin")));	
								}
								
								if(listTinmcxp.size()>0){
									integracionServicio.guardarCxP(listTinmcxp);
									exito = true;
								}else{
									exito = true;
									FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"",prop.getProperty("errorCxP")));	
								}
								
								if(tinmcodis.size()>0){
									integracionServicio.guardarConsigancionDirecta(tinmcodis);
									exito = true;
								}else{
									exito = true;
									FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"",prop.getProperty("errorCLiDir")));	
								}
								
								if(tinmrecads.size()>0){
									integracionServicio.guardarReciboCajaDirecta(tinmrecads);
									exito = true;
								}else{
									exito = true;
									FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"",prop.getProperty("errorRecaDir")));	
								}
								
								if(facturasDTOs.size()>0){
									integracionServicio.guardarFactura(facturasDTOs);
									exito = true;
								}else{
									exito = true;
									FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"",prop.getProperty("errorFactuGua")));
								}
								
								processMovi = false;
								cerrar();
								cerrarEPS();
								cerrarNhFec();
								cerrarCampus();
								cerrarSchool();
								cerrarSport();
								FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"",prop.getProperty("exitoCargueMovimientos")));
								FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("ipsmovi");
							}else{
								processMovi = false;
								cerrar();
								cerrarEPS();
								cerrarNhFec();
								cerrarCampus();
								cerrarSchool();
								cerrarSport();
								FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("ipsmovi");
							}
						}
						
					}else{
						dialConfM = false;
						processMovi = false;
						btnMovIPS = false;
						btnMovIPSConf = true;
						btnMovNh =false;
						btnMovNhConf = true;
						fileMovIps = null;
						fileMovNehot = null;
						dialConfFecNh= false;
						btnMovCampusConf = true;
						btnMovCampus = false;
						dialConfMCampus= false;
						dialConfFecSchool = false;
						btnMovSchool = false;
						btnMovSchoolConf = true;
						processMovi = false;
						sede = 0;
						sedeC = 0;
						sedeEps=0;
						sedeNh=0;
						sedeS=0;
						sedeSP=0;
						exito = false;
						FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_ERROR,"", prop.getProperty("errorDatos")));
						FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("ipsmovi");
					}

				}
			}else{
				exito = false;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("procesoActivo")));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			processMovi = false;
			exito = false;
			cerrar();
			cerrarEPS();
			cerrarNhFec();
			cerrarCampus();
			cerrarSchool();
			cerrarSport();
			FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_FATAL,"", prop.getProperty("errorMovimien")));
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("ipsmovi");
		}
	}
	
	
	/**
	 * METODO QUE SE ENCARGA D CREAR LA CUENA POR COBRAR 
	 * @param tinddemc
	 * @param archMoviDTO
	 * @author emedina
	 */
	private Tinmcxc crearCxC(Tinddemc tinddemc, EstructuraArchMoviDTO archMoviDTO,int opcion){
		Tinmcxc tinmcxc = new Tinmcxc();
		int prog =0;
		int sd = 0;
		boolean validaMovimiento = false;
		try {
			/*Se preparan los datos para la carga de cuentas por cobrar*/
			
			Tinmcuen cuent = new Tinmcuen();
			switch (opcion) {
			case MOVI_IPS:
				prog =2;
				sd = sede;
				validaMovimiento = archMoviDTO.getCamp9().equals("D");
				break;
			case MOVI_EPS:
				prog =3;
				sd = sedeEps;
				validaMovimiento = archMoviDTO.getCamp9().equals("D");
				break;
			case MOVI_NEW_HOTEL:
				prog =6;
				sd = sedeNh;
				validaMovimiento = true;
				break;
			case MOVI_SCHOOL_PACK:
				prog =7;
				sd = sedeSP;
				validaMovimiento = true;
				break;
			case MOVI_SPORT:
				prog =9;
				sd = sedeS;
				validaMovimiento = true;
				break;
			case MOVI_CAMPUS:
				prog =8;
				sd = sedeC;
				validaMovimiento = true;
				break;
			}
			
			cuent = integracionServicio.cuentaManejaDocumento(tinddemc.getDemcCueCodi(),sd,prog);
			
			if(cuent.getCuentSerial()>0){
				// pregunto si el campo mcon_sweb trae dato o es 5 genere cuenta por cobrar
				if(cuent.getCuentSweb()==5){
					if(validaMovimiento){
						tinmcxc.setCxcEmpCodi(tinpcxc.getPcxcEmpCodi());
						tinmcxc.setCxcTopCodi((short)cuent.getCuentTiopErp());
						tinmcxc.setCxcCxcNume(opcion==MOVI_NEW_HOTEL ? validaDatoReferncia(archMoviDTO.getCamp12()):Integer.valueOf(archMoviDTO.getCamp4())); //SI es IPS en el documento si es es el mismo,  para NEW HOTEL se saca del campo referencia 
						SimpleDateFormat dfcxc = new SimpleDateFormat("yyyy/MM/dd");
						String dfoFech = null;
						dfoFech  = dfcxc.format(archMoviDTO.getCamp2());
						tinmcxc.setCxcCxcFech(dfoFech);
						tinmcxc.setCxcCxcDesc(tinddemc.getDemcDmcDesc());
						tinmcxc.setCxcDclCodd(tinpcxc.getPcxcDclCodd());
						tinmcxc.setCxcCliCoda(tinddemc.getDemcTerCoda().trim());
						tinmcxc.setCxcArbCods(tinddemc.getDemcArbCods().trim());
						tinmcxc.setCxcCxcValo(tinddemc.getDemcDmcVacr()!=null && !tinddemc.getDemcDmcVacr().equals("") ? tinddemc.getDemcDmcVacr(): tinddemc.getDemcDmcVadb());
						tinmcxc.setCxcMonCodi(tinpcxc.getPcxcMonCodi());
						//adicion campos control de cambios 2019-05-07
						tinmcxc.setCxcTipCodi(cuent.getCuenTipCodi());
						tinmcxc.setCxcCueCodi(cuent.getCuenCueCodi().equals("") ? null : cuent.getCuenCueCodi());
						SimpleDateFormat dfcxc1 = new SimpleDateFormat("yyyy/MM/dd");
						String dfoFec = null;
						dfoFec = dfcxc1.format(archMoviDTO.getCamp2());
						tinmcxc.setCxcCxcFeta(dfoFec);
						tinmcxc.setCxcCxcRefe("0");
						tinmcxc.setCxcCxcFeve(dfoFec);
						tinmcxc.setCxcArbCoda(tinddemc.getDemcArbCoda().trim());
						tinmcxc.setCxcArbCodc(tinddemc.getDemcArbCodc().trim());
						tinmcxc.setCxcArbCodp(tinddemc.getDemcArbCodp().trim());
						tinmcxc.setCxcSedeSerial(sd);
						tinmcxc.setCxcProgSerial(prog);
						tinmcxc.setCxcEstado("L");
						tinmcxc.setCxcFCargue(new Date());
						
						if(archMoviDTO.getCamp1().substring(0,1).equals("F")){
							tinmcxc.setCxcCxcTipo("F");
						}else if(archMoviDTO.getCamp1().substring(0,2).equals("NC")){
							tinmcxc.setCxcCxcTipo("C");
						}else if(archMoviDTO.getCamp1().substring(0,2).equals("ND")){
							tinmcxc.setCxcCxcTipo("D");
						}else{
							tinmcxc.setCxcCxcTipo("F");
						}
						
						tinmcxc.setCxcCxcCond(0);
					}
				}
				
//				if(opcion == MOVI_NEW_HOTEL){
//					// si sweb2 tiene dato si es 9 llene cosignacion
//						tinmcolis.add(crearConsigancionEnLinea(archMoviDTO,opcion));
//					}
//					
//					//en linea si es 11 racibo de caja
//					if(cuent.getCuenSweb2()==11){
//						tinmrecas.add(crearReciboDeCaja(archMoviDTO,opcion));
//					}
//				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_FATAL,"", prop.getProperty("errorcXc")));
		}
		return tinmcxc;
	}
	
	/**
	 * METODO QUE SE ENCARGA D CREAR LA CUENA POR PAGAR
	 * @param tinddemc
	 * @param archMoviDTO
	 * @author emedina
	 */
	private Tinmcxp crearCxp(Tinddemc tinddemc, EstructuraArchMoviDTO archMoviDTO,int opcion){
		Tinmcxp tinmcxp = new Tinmcxp();
		int prog =0;
		int sd = 0;
		boolean validaMovimiento = false;
		try {
			/*Se preparan los datos para la carga de cuentas por cobrar*/
			
			Tinmcuen cuent = new Tinmcuen();
			switch (opcion) {
			case MOVI_IPS:
				prog =2;
				sd = sede;
				validaMovimiento = archMoviDTO.getCamp9().equals("D");
				break;
			case MOVI_EPS:
				prog =3;
				sd = sedeEps;
				validaMovimiento = archMoviDTO.getCamp9().equals("D");
				break;
			case MOVI_NEW_HOTEL:
				prog =6;
				sd = sedeNh;
				validaMovimiento = true;
				break;
			case MOVI_SCHOOL_PACK:
				prog =7;
				sd = sedeSP;
				validaMovimiento = archMoviDTO.getCamp9().equals("D");;
				break;
			case MOVI_SPORT:
				prog =9;
				sd = sedeS;
				validaMovimiento = archMoviDTO.getCamp9().equals("D");;
				break;
			case MOVI_CAMPUS:
				prog =8;
				sd = sedeC;
				validaMovimiento = archMoviDTO.getCamp9().equals("D");;
				break;
			}
			cuent = integracionServicio.cuentaManejaDocumento(tinddemc.getDemcCueCodi(),sd,prog);
			
			if(cuent.getCuentSerial()>0){
				// pregunto si el campo mcon_sweb trae dato o es 5 genere cuenta por cobrar
				if(cuent.getCuentSweb()==15){
					if(validaMovimiento){
						SimpleDateFormat dfcxc = new SimpleDateFormat("yyyyMMdd");
						tinmcxp.setMcxpEmpCodi(tinpcxp.getPcxpEmpCodi());
						tinmcxp.setMcxpTopCodi(cuent.getCuentTiopErp());
						tinmcxp.setMcxpCxpNume(opcion==MOVI_NEW_HOTEL? 0:validaDatoReferncia(archMoviDTO.getCamp12()));
						tinmcxp.setMcxpCxpDesc(tinddemc.getDemcDmcDesc());
						tinmcxp.setMcxpArbCoda(tinddemc.getDemcArbCoda().trim());
						tinmcxp.setMcxpArbCodc(tinddemc.getDemcArbCodc().trim());
						tinmcxp.setMcxpArbCods(tinddemc.getDemcArbCods().trim());
						tinmcxp.setMcxpArbCodp(tinddemc.getDemcArbCodp().trim());
						tinmcxp.setMcxpCflCodi(tinpcxp.getPcxcCflCodi());
						tinmcxp.setMcxpCueCodi(tinpcxp.getPcxpCueCodi());
						tinmcxp.setMcxpCxpCont(0);
						String nfech = null;
						nfech = dfcxc.format(archMoviDTO.getCamp2());
						tinmcxp.setMcxpCxpNech(new BigDecimal(nfech));
						tinmcxp.setMcxpCxpNeve(nfech);
						tinmcxp.setMcxpCxpNumer(0);
						tinmcxp.setMcxpCxpTipo("F");
						tinmcxp.setMcxpCxpTota(new BigDecimal(archMoviDTO.getCamp8()));
						tinmcxp.setMcxpDepCodd(tinpcxp.getPcxpDepCodd());
						tinmcxp.setMcxpEstado("L");
						tinmcxp.setMcxpFacNfap(archMoviDTO.getCamp12());
						tinmcxp.setMcxpFCargue(new Date());
						tinmcxp.setMcxpIteCodi(tinpcxp.getPcxpIteCodi());
						tinmcxp.setMcxpMonCodi(tinpcxp.getPcxpMonCodi());
						tinmcxp.setMcxpProgSerial(prog);
						tinmcxp.setMcxpPvdCoda(tinddemc.getDemcTerCoda().trim());
						tinmcxp.setMcxpRetorno((short)0);
						tinmcxp.setMcxpSedeSerial(sd);
						tinmcxp.setMcxpTerCodp("0");
						tinmcxp.setMcxpTxerror("");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_FATAL,"", prop.getProperty("errorcXc")));
		}
		return tinmcxp;
	}
	
	/**
	 * metodo que crea el objeto de recibos de caja
	 * @param archMoviDTO
	 * @return
	 * @author emedina
	 */
	private Tinmreca crearReciboDeCaja(EstructuraArchMoviDTO archMoviDTO, int opcion,int sd, int prog){
		Tinmreca tinmreca = new Tinmreca();
		try{
			Timdderc timdderc = new Timdderc();
			Tinmfopa tinmfopa = new Tinmfopa();
			Tinmimrc tinmimrc = new Tinmimrc();
			List<Tinmfopa> tinmfopas = new ArrayList<Tinmfopa>();
			List<Timdderc> timddercs = new ArrayList<Timdderc>();
			List<Tinmimrc> tinmimrcs = new ArrayList<Tinmimrc>();
			
			if(archMoviDTO.getCamp9().equals("C")){
				/*se crea el maestro del recibo de caja*/
				tinmreca.setRecaEmpCodi(tinpreca.getPrecaEmpCodi());
				tinmreca.setRecaTopCodi(tinpreca.getPrecaTopCodi());
				tinmreca.setRecaMteNume(opcion == MOVI_IPS ? Integer.valueOf(archMoviDTO.getCamp4()):0);
				tinmreca.setRecaMteCont(0);
				String fecha = null;
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
				fecha = df.format(archMoviDTO.getCamp2());
				tinmreca.setRecaMteFech(fecha);
				tinmreca.setRecaArbCsuc(tinpreca.getPrecaArbCsuc().trim());
				tinmreca.setRecaMteDesc(opcion == MOVI_IPS ? archMoviDTO.getCamp10(): "0"); //si es IPs en el documento si es es el mismo,  para NEW HOTEL 0
				tinmreca.setRecaTerCoda(archMoviDTO.getCamp6());
				tinmreca.setRecaCflCodi(tinpreca.getPrecaCflCodi());
				tinmreca.setRecaMonCodi((short) tinpreca.getPrecaMonCodi());
				String mteFeta = null;
				mteFeta = df.format(archMoviDTO.getCamp2());
				tinmreca.setRecaMteFeta(mteFeta); 
				tinmreca.setRecaCajCodi((short)tinpreca.getPrecaCajCodi());
				tinmreca.setProgSerial(tinpreca.getProgSerial());
				tinmreca.setSedeSerial(tinpreca.getSedeSerial());
				tinmreca.setRecaEstado("L");
				tinmreca.setRecaFCargue(new Date());
				tinmreca.setRecVenCodi(1);
				tinmreca.setRecNumMovi(opcion == MOVI_IPS ? Integer.parseInt(archMoviDTO.getCamp12()): validaDatoReferncia(archMoviDTO.getCamp12()));
				tinmreca.setRecTipMovi(archMoviDTO.getCamp20());
				
				
				/*guarda el detalle de la forma de pago por movimiento*/
				tinmfopa.setTinmreca(tinmreca);
				tinmfopa.setFopaFopCodi(tinpreca.getPrecaFopCodi()); 
				tinmfopa.setFopaDfoValo(BigDecimal.valueOf(Double.valueOf(archMoviDTO.getCamp8())));
				tinmfopa.setFopaBanCodi(tinpreca.getPrecaBanCodi());
				tinmfopa.setFopaTacCodi(tinpreca.getPrecaTacCodi());
				SimpleDateFormat dfb = new SimpleDateFormat("yyyyMMdd");
				String dfoFech = null;
				dfoFech= dfb.format(new Date());
				tinmfopa.setFopaDfoFech(Integer.valueOf(dfoFech));
				tinmfopa.setFopaDfoCedu(BigDecimal.ZERO); 
				tinmfopa.setFopaDfoNomg(".");
				tinmfopa.setFopaDfoVcom(BigDecimal.ZERO); 
				tinmfopa.setFopaDfoViva(BigDecimal.ZERO);
				tinmfopas.add(tinmfopa);
				tinmreca.setTinmfopas(tinmfopas);
				
				/*guarda el detalle de los recibos de caja*/
				//timdderc.setTinmreca(tinmreca);
				// mirar en el documento si es es el mismo  para IPS, en NEW HOTEL se saca del campo referencia
				if(opcion == MOVI_IPS){
					timdderc.setDercCxcNume(Integer.valueOf(archMoviDTO.getCamp12()));
				}else if(opcion == MOVI_NEW_HOTEL){
					timdderc.setDercCxcNume(validaDatoReferncia(archMoviDTO.getCamp12()));
				}
				  
				String cxcNech=null;
				cxcNech = dfb.format(new Date());
				timdderc.setDercCxcNech(Integer.valueOf(cxcNech));
				timdderc.setDercArbCsuc(tinpreca.getPrecaArbCsuc().trim());
				timdderc.setDercTopCcxc(integracionServicio.tipoOperacion(archMoviDTO.getCamp11(),sd,prog)); //tabla parametrica de componente tipo opeacion se envia el tipo de movinientp campo 9 de arcghivo
				timdderc.setDercCxcCont(0);
				timdderc.setDercCxcCref(archMoviDTO.getCamp5());
				timdderc.setDercCxcNume(opcion == MOVI_IPS ? Integer.parseInt(archMoviDTO.getCamp12()):validaDatoReferncia(archMoviDTO.getCamp12()));
				timdderc.setDercRtsValo(new BigDecimal(archMoviDTO.getCamp8()));
				
				//inpuestos
				tinmimrc.setTimdderc(timdderc);
				tinmimrc.setImrcImpCodi((short)tinpreca.getPrecaImpCodi()); 
				tinmimrc.setImrcDstCodi((short)tinpreca.getPrecaDstCodi()); 
				tinmimrc.setImrcRdtImds("D"); 
				tinmimrc.setImrcArbCsuc(tinmreca.getRecaArbCsuc());
				tinmimrc.setImrcRdtValo(BigDecimal.valueOf(Double.valueOf(archMoviDTO.getCamp8())));
				tinmimrcs.add(tinmimrc);
				timdderc.setTinmreca(tinmreca);
				timddercs.add(timdderc);
				tinmreca.setTinmfopas(tinmfopas);
				tinmreca.setTimddercs(timddercs);
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_FATAL,"", prop.getProperty("errorRc")));
		}
		
		return tinmreca;
	}
	
	/**
	 * metodo que crea las consignaciones directas
	 * @param archMoviDTO
	 * @return
	 */
	public ConsignacionDirectaDTO crearConsignacionDirecta(EstructuraArchMoviDTO archMoviDTO,int opcion, Tinddemc tinddemc){
		ConsignacionDirectaDTO consignacionDirectaDTO = new ConsignacionDirectaDTO();
		try {
			Tinmcuen cuent = new Tinmcuen();
			Tinmcodi tinmcodi = new Tinmcodi();
			List<Tinddecd> tinddecds = new ArrayList<Tinddecd>();
			List<Tindcdda> tindcddas = new ArrayList<Tindcdda>();
			int prog =0;
			int sd = 0;
			boolean validaMovimiento = false;
			switch (opcion) {
			case MOVI_IPS:
				prog =2;
				sd = sede;
				validaMovimiento = archMoviDTO.getCamp9().equals("D");
				break;
			case MOVI_EPS:
				prog =3;
				sd = sedeEps;
				validaMovimiento = archMoviDTO.getCamp9().equals("D");
				break;
			case MOVI_NEW_HOTEL:
				prog =6;
				sd = sedeNh;
				validaMovimiento = true;
				break;
			case MOVI_SCHOOL_PACK:
				prog =7;
				sd = sedeSP;
				validaMovimiento = true;//archMoviDTO.getCamp9().equals("D");
				break;
			case MOVI_SPORT:
				prog =9;
				sd = sedeS;
				validaMovimiento = true;//archMoviDTO.getCamp9().equals("D");
				break;
			case MOVI_CAMPUS:
				prog =8;
				sd = sedeC;
				validaMovimiento = true;//archMoviDTO.getCamp9().equals("D");
				break;
			}
			
			cuent = integracionServicio.cuentaManejaDocumento(tinddemc.getDemcCueCodi(),sd,prog);
			
			if(cuent.getCuentSerial()>0){
				if(cuent.getCuentSweb()==10){
					if(validaMovimiento){
						SimpleDateFormat fom = new SimpleDateFormat("dd/MM/yyyy");
						tinmcodi.setCodiEmpCodi(tinpcodi.getPcodiEmpCodi());
						tinmcodi.setCodiTopCodi(tinpcodi.getPcodiTopCodi());
						tinmcodi.setCodiCubNume(cuent.getCuentCubNume().trim());
						tinmcodi.setCodiArbCods(tinddemc.getDemcArbCods().trim());
						tinmcodi.setCodiCflCodi(tinpcodi.getPcodiCflCodi());
						if((tinddemc.getDemcCueCodi().substring(0,3).equals("1110") || tinddemc.getDemcCueCodi().substring(0,3).equals("1120")) && tinddemc.getDemcDmcVacr().intValue()>0){
							tinmcodi.setCodiEstado(prog==7?"A":"L");
						}else{
							tinmcodi.setCodiEstado("L");
						}
						tinmcodi.setCodiFCargue(new Date());
						tinmcodi.setCodiMonCodi(tinpcodi.getPcodiMonCodi());
						tinmcodi.setCodiMteDesc(archMoviDTO.getCamp10());
						String feci = fom.format(archMoviDTO.getCamp2());
						tinmcodi.setCodiMteFcon(feci);
						tinmcodi.setCodiMteFech(feci);
						tinmcodi.setCodiMteFeta(feci);
						tinmcodi.setCodiMteFopa("E");
						tinmcodi.setCodiMteNume(Integer.valueOf(archMoviDTO.getCamp4()));
						tinmcodi.setCodiMteRecd("0");
						tinmcodi.setCodiMteTdis("A");
						tinmcodi.setCodiProgSerial(prog);
						tinmcodi.setCodiRegInve(".");
						tinmcodi.setCodiRetorno(0);
						tinmcodi.setCodiSedeSerial(sd);
						tinmcodi.setCodiTerCoda(archMoviDTO.getCamp6());
						tinmcodi.setCodiTxerror("");
						consignacionDirectaDTO.setTinmcodi(tinmcodi);
						
						Tinddecd tinddecd = new Tinddecd();
						tinddecd.setDecdCflCodd(tinpcodi.getPcodiCflCodd());
						tinddecd.setDecdCieCodi(tinpcodi.getPcodiCieCodi());
						tinddecd.setDecdRtsRefe(".");
						tinddecd.setDecdRtsValo(new BigDecimal(archMoviDTO.getCamp8()));
						tinddecd.setDecdTerCodd(tinddemc.getDemcTerCoda());
						tinddecd.setDecdFech(feci);
						tinddecds.add(tinddecd);
						consignacionDirectaDTO.setTinddecds(tinddecds);
						
						for(int i=1; i<=4; i++){
							Tindcdda tindcdda = new Tindcdda();
							switch (i) {
								case 1:
									tindcdda.setDcddaTarCodi((short)1);
									tindcdda.setDcddaArbCodi(tinddemc.getDemcArbCoda());
									tindcdda.setDcddaDmtPorc(new BigDecimal(100));
									tindcdda.setDcddaDmtTipo("P");
									tindcdda.setDcddaDmtValo(BigDecimal.ZERO);
									break;
								case 2:
									tindcdda.setDcddaTarCodi((short)2);
									tindcdda.setDcddaArbCodi(tinddemc.getDemcArbCods());
									tindcdda.setDcddaDmtPorc(new BigDecimal(100));
									tindcdda.setDcddaDmtTipo("P");
									tindcdda.setDcddaDmtValo(BigDecimal.ZERO);
									break;
								case 3:
									tindcdda.setDcddaTarCodi((short)3);
									tindcdda.setDcddaArbCodi(tinddemc.getDemcArbCodc());
									tindcdda.setDcddaDmtPorc(new BigDecimal(100));
									tindcdda.setDcddaDmtTipo("P");
									tindcdda.setDcddaDmtValo(BigDecimal.ZERO);
									break;
								case 4:
									tindcdda.setDcddaTarCodi((short)4);
									tindcdda.setDcddaArbCodi(tinddemc.getDemcArbCodp());
									tindcdda.setDcddaDmtPorc(new BigDecimal(100));
									tindcdda.setDcddaDmtTipo("P");
									tindcdda.setDcddaDmtValo(BigDecimal.ZERO);
									break;
							}
							tindcddas.add(tindcdda);
						}
						consignacionDirectaDTO.setTindcddas(tindcddas);
					}
				}else{
					consignacionDirectaDTO = null;
				}
			}else{
				consignacionDirectaDTO = null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_FATAL,"", prop.getProperty("errorCLiD")));
		}
		
		return consignacionDirectaDTO;
	}
	
	
	public ReciboDirectoDTO crearReciboCajaDirecta(EstructuraArchMoviDTO archMoviDTO,int opcion, Tinddemc tinddemc){
		ReciboDirectoDTO reciboDirectoDTO = new ReciboDirectoDTO();
		try {
			Tinmcuen cuent = new Tinmcuen();
			Tinmrecad tinmrecad = new Tinmrecad();
			Tindfopa tindfopa = new Tindfopa();
			List<Tindrcdi> tindrcdis = new ArrayList<Tindrcdi>();
			List<Tindrcda> tindrcdas = new ArrayList<Tindrcda>();
			
			int prog =0;
			int sd = 0;
			boolean validaMovimiento = false;
			switch (opcion) {
			case MOVI_IPS:
				prog =2;
				sd = sede;
				validaMovimiento = archMoviDTO.getCamp9().equals("D");
				break;
			case MOVI_EPS:
				prog =3;
				sd = sedeEps;
				validaMovimiento = archMoviDTO.getCamp9().equals("D");
				break;
			case MOVI_NEW_HOTEL:
				prog =6;
				sd = sedeNh;
				validaMovimiento = true;
				break;
			case MOVI_SCHOOL_PACK:
				prog =7;
				sd = sedeSP;
				validaMovimiento = archMoviDTO.getCamp9().equals("D");;
				break;
			case MOVI_SPORT:
				prog =9;
				sd = sedeS;
				validaMovimiento = archMoviDTO.getCamp9().equals("D");;
				break;
			case MOVI_CAMPUS:
				prog =8;
				sd = sedeC;
				validaMovimiento = archMoviDTO.getCamp9().equals("D");;
				break;
			}
			
			cuent = integracionServicio.cuentaManejaDocumento(tinddemc.getDemcCueCodi(),sd,prog);
			
			if(cuent.getCuentSerial()>0){
				if(cuent.getCuentSweb()==13){
					if(validaMovimiento){
						SimpleDateFormat fom = new SimpleDateFormat("dd/MM/yyyy");
						tinmrecad.setRecadArbCods(tinddemc.getDemcArbCods());
						tinmrecad.setRecadCajCodi(tinprecad.getPrecadCajCodi());
						tinmrecad.setRecadCflCodi(tinprecad.getPrecadCflCodi());
						tinmrecad.setRecadEmpCodi(tinprecad.getPrecadEmpCodi());
						if(tinddemc.getDemcCueCodi().substring(0,3).equals("1105") && tinddemc.getDemcDmcVacr().intValue()>0){
							tinmrecad.setRecadEstado(prog==7?"A":"L");
						}else{
							tinmrecad.setRecadEstado("L");
						}
						tinmrecad.setRecadFCargue(new Date());
						tinmrecad.setRecadMonCodi(tinprecad.getPrecadMonCodi());
						tinmrecad.setRecadMteCont(0);
						tinmrecad.setRecadMteDesc(tinddemc.getDemcDmcDesc());
						tinmrecad.setRecadMteEsta("A");
						String feci = fom.format(archMoviDTO.getCamp2());
						tinmrecad.setRecadMteFech(feci);
						tinmrecad.setRecadMteFeta(feci);
						tinmrecad.setRecadMteNuco(0);
						tinmrecad.setRecadMteNume(0);
						tinmrecad.setRecadMteTdis("A");
						tinmrecad.setRecadProgSerial(prog);
						tinmrecad.setRecadRegInve(".");
						tinmrecad.setRecadRetono((short) 0);
						tinmrecad.setRecadSedeSerial(sd);
						tinmrecad.setRecadTerCoda(tinddemc.getDemcTerCoda());
						tinmrecad.setRecadTopCodi(tinprecad.getPrecadTopCodi());
						tinmrecad.setRecadVenCodi(Float.valueOf(tinprecad.getPrecadVenCodi().toString()));
						tinmrecad.setRecadTipMov(archMoviDTO.getCamp20());
						reciboDirectoDTO.setTinmrecad(tinmrecad);
						
						Tindrcdi tindrcdi = new Tindrcdi();
						tindrcdi.setRcdiCflCodd(tinprecad.getPrecadCflCodd());
						tindrcdi.setRcdiCieCodi(tinprecad.getPrecadCieCodi());
						tindrcdi.setRcdiRtsRefe(".");
						tindrcdi.setRcdiRtsValo(Float.valueOf(archMoviDTO.getCamp8()));
						tindrcdi.setRcdiTerCodd(tinddemc.getDemcTerCoda());
						tindrcdi.setRcdifech(feci);
						tindrcdis.add(tindrcdi);
						reciboDirectoDTO.setTindrcdis(tindrcdis);
						
						for(int i=1; i<=4; i++){
							Tindrcda tindrcda = new Tindrcda();
							
							switch (i) {
								case 1:
									tindrcda.setDrcdaDmtTipo("P");
									tindrcda.setDrcdaDmtPorc(new BigDecimal(100));
									tindrcda.setDrcdaArbCodi(tinddemc.getDemcArbCoda());
									tindrcda.setDrcdaDmtValo(BigDecimal.ZERO);
									tindrcda.setDrcdaTarCodi((short)1);
									break;
								case 2:
									tindrcda.setDrcdaDmtTipo("P");
									tindrcda.setDrcdaDmtPorc(new BigDecimal(100));
									tindrcda.setDrcdaArbCodi(tinddemc.getDemcArbCods());
									tindrcda.setDrcdaDmtValo(BigDecimal.ZERO);
									tindrcda.setDrcdaTarCodi((short)2);
									break;
								case 3:
									tindrcda.setDrcdaDmtTipo("P");
									tindrcda.setDrcdaDmtPorc(new BigDecimal(100));
									tindrcda.setDrcdaArbCodi(tinddemc.getDemcArbCodc());
									tindrcda.setDrcdaDmtValo(BigDecimal.ZERO);
									tindrcda.setDrcdaTarCodi((short)3);
									break;
								case 4:
									tindrcda.setDrcdaDmtTipo("P");
									tindrcda.setDrcdaDmtPorc(new BigDecimal(100));
									tindrcda.setDrcdaArbCodi(tinddemc.getDemcArbCodp());
									tindrcda.setDrcdaDmtValo(BigDecimal.ZERO);
									tindrcda.setDrcdaTarCodi((short)4);
									break;
							}
							
							tindrcdas.add(tindrcda);
						}
						
						tindfopa.setFopaFpaCodi(1);
						tindfopa.setFopaTacCodi(0);
						tindfopa.setFopaDfoFech(feci);
						tindfopa.setFopaDfoValo(Float.valueOf(archMoviDTO.getCamp8()));
						tindfopa.setFopaDfoViva(BigDecimal.ZERO);
						tindfopa.setFopaBanCodi(0);
						tindfopa.setFopaDfoChec(BigDecimal.ZERO);
						tindfopa.setFopaDfoNocu("0");
						tindfopa.setFopaDfoChep("N");
						tindfopa.setFopaDfoCedu(BigDecimal.ZERO);
						tindfopa.setFopaDfoNomg(".");
						tindfopa.setFopaDfoClav(".");
						tindfopa.setFopaDfoBase(BigDecimal.ZERO);
						
						reciboDirectoDTO.setTindrcdas(tindrcdas);
						reciboDirectoDTO.setTindfopa(tindfopa);
						
					}
				}else{
					reciboDirectoDTO = null;
				}
			}else{
				reciboDirectoDTO = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_FATAL,"", prop.getProperty("errorReca")));
		}
		return reciboDirectoDTO;
	}
	
	/**
	 * metodo que crea la consignacion en lineas
	 * @return
	 */
	public ConsigancionLineaDTO crearConsigancionEnLinea(EstructuraArchMoviDTO archMoviDTO,int opcion,int servicio, Tinmcuen cuent){
		ConsigancionLineaDTO consigancionLineaDTO = new ConsigancionLineaDTO();
		try {
			Tinmcoli tinmcoli = new Tinmcoli();
			List<Tinddede> tinddedes = new ArrayList<Tinddede>();
			List<Tinddecl> tinddecls = new ArrayList<Tinddecl>();
			String tempCad2[] = null;
			String tempCad22[] = null;
			int prog =0;
			int sd = 0;
			boolean validaMovimiento= false;
			switch (opcion) {
			case MOVI_IPS:
				prog =2;
				sd = sede;
				validaMovimiento = archMoviDTO.getCamp9().equals("D");
				break;
			case MOVI_EPS:
				prog =3;
				sd = sedeEps;
				validaMovimiento = archMoviDTO.getCamp9().equals("D");
				break;
			case MOVI_NEW_HOTEL:
				prog =6;
				sd = sedeNh;
				validaMovimiento = true;
				break;
			case MOVI_SCHOOL_PACK:
				prog =7;
				sd = sedeSP;
				validaMovimiento = true;
				break;
			case MOVI_SPORT:
				prog =9;
				sd = sedeS;
				validaMovimiento = true;
				break;
			case MOVI_CAMPUS:
				prog =8;
				sd = sedeC;
				validaMovimiento = true;
				break;
			}
			
			//cuent = integracionServicio.cuentaManejaDocumento(cuentaErp,sd,prog);
			
			//if(cuent.getCuentSerial()>0){
				//if(cuent.getCuentSweb()==9){
					if(validaMovimiento){
						if(!archMoviDTO.getCamp12().equals("0")){
							tinmcoli.setColiEmpCodi(tinpcoli.getPcoliEmpCodi());
							tinmcoli.setColiTopCodi(tinpcoli.getPcoliTopCodi());
							tinmcoli.setColiMteNume(Integer.valueOf(archMoviDTO.getCamp4()));
							SimpleDateFormat fom = new SimpleDateFormat("dd/MM/yyyy");
							String feci = fom.format(archMoviDTO.getCamp2());
							tinmcoli.setColiMteFech(feci);
							String feco = fom.format(archMoviDTO.getCamp21());
							tinmcoli.setColiMteFcon(feco);
							tinmcoli.setColiMteDesc(archMoviDTO.getCamp10());
							tinmcoli.setColiMteFopa("E");
							tinmcoli.setColiTerCoda(archMoviDTO.getCamp6());
							tinmcoli.setColiCflCodi(tinpcoli.getPcoliCflCodi());
							tinmcoli.setColiArbCods(tinpcoli.getPcoliArbCods().trim());
							//integracionServicio.consltarCuenta(archMoviDTO.getCamp5(),prog,sd).toString()
							System.out.println("ERP-->HERRAMIENTAS: CUB_NUME COLI "+cuent.getCuentCubNume());
							tinmcoli.setColiCubNume(cuent.getCuentCubNume()); 
							tinmcoli.setColiBanCodc((short)0);
							tinmcoli.setColiMonCodi(tinpcoli.getPcoliMonCodi());
							tinmcoli.setColiMteFeta(feco);
							
							if(archMoviDTO.getCamp10().contains("-")){
								tempCad2 = archMoviDTO.getCamp10().split("-");
								if(tempCad2[1].toString().matches("^\\d+$")){
									tinmcoli.setColiMteChec(new BigDecimal(tempCad2[1].toString()));
								}else{
									tempCad22 = tempCad2[1].toString().split(" ");
									if(opcion==MOVI_SCHOOL_PACK){
										tinmcoli.setColiMteChec(new BigDecimal(tempCad22[0].toString().substring(0, tempCad22[0].toString().length())));
									}else{
										tinmcoli.setColiMteChec(new BigDecimal(tempCad22[0].toString().substring(0, tempCad22[0].toString().length()-5)));
									}
									
								}
							}else{
								tinmcoli.setColiMteChec(BigDecimal.ZERO);
							}
							
							tinmcoli.setColiMteValo(new BigDecimal(archMoviDTO.getCamp8()));
							tinmcoli.setColiEstado("L");
							tinmcoli.setColiFCargue(new Date());
							tinmcoli.setColiProgSerial(tinpcoli.getProgSerial());
							tinmcoli.setColiSedeSerial(tinpcoli.getSedeSerial());
							consigancionLineaDTO.setTinmcoli(tinmcoli);
							
							Tinddecl tinddecl = new Tinddecl();
							Tinddede tinddede = new Tinddede();
			
							tinddecl.setDeclCflCodd(tinpcoli.getPcoliCflCodi());
							tinddecl.setDeclCieCodi(tinpcoli.getPcoliCieCodi());
							int cxcCont = integracionServicio.traerCxCCont(prog, Integer.parseInt(archMoviDTO.getCamp12()), archMoviDTO.getCamp6(), archMoviDTO.getCamp10());
							tinddecl.setDeclCxcCont(cxcCont);
							tinddecl.setDeclFacCont(Integer.parseInt(archMoviDTO.getCamp12()));
							tinddecl.setDeclRtsValo(new BigDecimal(archMoviDTO.getCamp8()));
			
							tinddede.setDedeArbCsuc(tinpcoli.getPcoliArbCods());
							tinddede.setDedeDstCodi(Short.valueOf(String.valueOf(tinpcoli.getPcoliDstCodi())));
							tinddede.setDedeImpCodi(Short.valueOf(String.valueOf(tinpcoli.getPcoliImpCodi())));
							tinddede.setDedeRdtImds("I");
							tinddede.setDedeRdtValo(BigDecimal.ZERO);
							tinddede.setDedeSerial(tinpcoli.getSedeSerial());
							tinddede.setTinddecl(tinddecl);
			
							tinddedes.add(tinddede);
							tinddecls.add(tinddecl);
							tinddecl.setTinddedes(tinddedes);
			
							consigancionLineaDTO.setTinddecls(tinddecls);
							consigancionLineaDTO.setTinddedes(tinddedes);
						}else{
							
						}
					}
					
			if(tinmcoli.getColiMteNume()== 0){
				consigancionLineaDTO = null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_FATAL,"", prop.getProperty("errorCoLi")));
		}
		
		return consigancionLineaDTO;
	}
	
	/**
	 * Metodo que se encarga de crear la facturas y los cruces para colegios
	 * @param archMoviDTO
	 * @param opcion
	 * @param servicio
	 * @param cuent
	 * @return
	 */
	public FacturasDTO crearFacturaCruce(EstructuraArchMoviDTO archMoviDTO,int opcion, Tinddemc tinddemc){
		FacturasDTO facturasDTO = new FacturasDTO();
		try {
			
			Tinmfact tinmfact = new Tinmfact();
			Tindfact tindfact = new Tindfact();
			List<Tindfada> tindfadas = new ArrayList<Tindfada>();
			Tindfacr tindfacr = new Tindfacr();
			List<Tindfacr> tindfacrs= new ArrayList<Tindfacr>();
			int prog =0;
			int sd = 0;
			boolean validaMovimiento= false;
			switch (opcion) {
				case MOVI_SCHOOL_PACK:
					prog =7;
					sd = sedeSP;
					validaMovimiento = archMoviDTO.getCamp9().equals("C");
					break;
				case MOVI_SPORT:
					prog =9;
					sd = sedeS;
					validaMovimiento = archMoviDTO.getCamp9().equals("C");
					break;
				case MOVI_CAMPUS:
					prog =8;
					sd = sedeC;
					validaMovimiento = archMoviDTO.getCamp9().equals("C");
					break;
			}
			
			if(validaMovimiento){
				if(!archMoviDTO.getCamp12().equals("0")){
					SimpleDateFormat fomFech = new SimpleDateFormat("yyyy/MM/dd");
					tinmfact.setFactArbCsuc(tinddemc.getDemcArbCods());
					tinmfact.setFactCliCoda(archMoviDTO.getCamp6());
					tinmfact.setFactCont(0);
					tinmfact.setFactDclCodd((short)tinpfact.getPfactDclCodd());
					tinmfact.setFactEmpCodi(tinpfact.getPfactEmpCodi());
					tinmfact.setFactEstado("L");
					tinmfact.setFactFacCref(".");
					String fechaArchivo = fomFech.format(archMoviDTO.getCamp2());
					Date fechas = fomFech.parse(fechaArchivo);
					tinmfact.setFactFacDesc(fechaArchivo);
					tinmfact.setFactFacFecf(fechas);
					tinmfact.setFactFacFech(fechas);
					tinmfact.setFactFacFeci(fechas);
					tinmfact.setFactFacFepe(fechas);
					tinmfact.setFactFacFepo(fechas);
					tinmfact.setFactFacFeta(fechaArchivo);
					tinmfact.setFactFacFext(fechas);
					tinmfact.setFactFacNume(Integer.valueOf(archMoviDTO.getCamp4()));
					tinmfact.setFactNume(0);
					tinmfact.setFactFacPepe(BigDecimal.ZERO);
					tinmfact.setFactFacPeri("");
					tinmfact.setFactFacPext(BigDecimal.ZERO);
					tinmfact.setFactFacTdis("A");
					tinmfact.setFactFacTido("M");
					tinmfact.setFactFacTipo("C");
					tinmfact.setFactFCargue(new Date());
					tinmfact.setFactMcoCont(0);
					tinmfact.setFactMonCodi(tinpfact.getPfactMonCodi());
					tinmfact.setFactProgSerial(tinpfact.getPfactProgSerial());
					tinmfact.setFactRetorno((short)0);
					tinmfact.setFactSedeSerial(tinpfact.getPfactSedeSerial());
					tinmfact.setFactTopCodi((short) tinpfact.getPfactTopCodi());
					tinmfact.setFactTxerror("");
					
					tindfact.setDfactBodCodi(tinpfact.getPfactBodCodi());
					tindfact.setDfactCtrCont(0);
					tindfact.setDfactDfaCant(1f);
					tindfact.setDfactDfaDesc(archMoviDTO.getCamp10());
					tindfact.setDfactDfaDest(tinpfact.getPfactDfaDest());
					tindfact.setDfactDfaPvde(0f);
					tindfact.setDfactDfaTide("P");
					tindfact.setDfactDfaValo(0);
					tindfact.setDfactProCodi(tinpfact.getPfactProCodi());
					tindfact.setDfactUniCodi((short)1);
					
					for(int i=1; i<=4; i++){
						Tindfada tindfada = new Tindfada();
						switch (i) {
							case 1:
								tindfada.setDfadaArbCodi(tinddemc.getDemcArbCoda());
								tindfada.setDfadaDdiPorc(new BigDecimal(100));
								tindfada.setDfadaDdiTipo("P");
								tindfada.setDfadaDdiValo(BigDecimal.ZERO);
								tindfada.setDfadaTarCodi((short)1);
								break;
							case 2:
								tindfada.setDfadaArbCodi(tinddemc.getDemcArbCods());
								tindfada.setDfadaDdiPorc(new BigDecimal(100));
								tindfada.setDfadaDdiTipo("P");
								tindfada.setDfadaDdiValo(BigDecimal.ZERO);
								tindfada.setDfadaTarCodi((short)2);
								break;
							case 3:
								tindfada.setDfadaArbCodi(tinddemc.getDemcArbCodc());
								tindfada.setDfadaDdiPorc(new BigDecimal(100));
								tindfada.setDfadaDdiTipo("P");
								tindfada.setDfadaDdiValo(BigDecimal.ZERO);
								tindfada.setDfadaTarCodi((short)3);
								break;
							case 4:
								tindfada.setDfadaArbCodi(tinddemc.getDemcArbCodp());
								tindfada.setDfadaDdiPorc(new BigDecimal(100));
								tindfada.setDfadaDdiTipo("P");
								tindfada.setDfadaDdiValo(BigDecimal.ZERO);
								tindfada.setDfadaTarCodi((short)4);
								break;
						}
						tindfadas.add(tindfada);
					}
					
					Tinmcxc tinmcxc = new Tinmcxc();
					tinmcxc = integracionServicio.traerCxCContFac(archMoviDTO.getCamp6(), archMoviDTO.getCamp12());
					tindfacr.setFacrArbSucc(tinddemc.getDemcArbCods());
					tindfacr.setFacrCruValo(new BigDecimal(archMoviDTO.getCamp8()));
					tindfacr.setFacrCxcCont(tinmcxc.getCxcCxcCont());
					Date fecFact=null;
					if(tinmcxc.getCxcCxcFech()!=null && !tinmcxc.getCxcCxcFech().equals("")){
						if(tinmcxc.getCxcEstado().equals("A")){
							String dia= tinmcxc.getCxcCxcFech().substring(0,2);
							String mes = tinmcxc.getCxcCxcFech().substring(3,5);
							String anio = tinmcxc.getCxcCxcFech().substring(6,10);
							fecFact= fomFech.parse(anio.concat("/").concat(mes).concat("/").concat(dia));
						}else{
							fecFact= fomFech.parse(tinmcxc.getCxcCxcFech());
						}
						
					}					 
					tindfacr.setFacrFacFecc((fecFact!=null && !fecFact.equals("")) ? fecFact:archMoviDTO.getCamp2());
					tindfacr.setFacrFacNumc(Integer.valueOf(archMoviDTO.getCamp12()));
					tindfacr.setFacrTopCodc(tinmcxc.getCxcTopCodi()>0 ? tinmcxc.getCxcTopCodi() : Short.valueOf(String.valueOf(tinpfact.getPfactTopCodi())));
					
					tindfacrs.add(tindfacr);
					facturasDTO.setTinmfact(tinmfact);
					facturasDTO.setTindfact(tindfact);
					facturasDTO.setTindfadas(tindfadas);
					facturasDTO.setTindfacr(tindfacrs);
					
					if(tinmfact.getFactFacNume()==0){
						facturasDTO = null;
					}
					
				}else if(tinmfact.getFactFacNume()==0){
					facturasDTO = null;
				}
				
			}else{
				if(tinmfact.getFactFacNume()==0){
					facturasDTO = null;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_FATAL,"", prop.getProperty("errorFacturas")));
		}
		
		return facturasDTO;
	}
	
	/**
	 * metodo que se encarga de valida las referencias de New Hotel
	 * @param referencia
	 * @return
	 * @author emedina
	 */
	private Integer validaDatoReferncia(String referencia){
		Integer ref = null;
		SimpleDateFormat formt = new SimpleDateFormat("ddMMyyyy");
		String refencia = null;
		if(referencia!=null && !referencia.equals("")){
			if(referencia.equals("TPV")){
				ref = Integer.valueOf(formt.format(new Date()));
			}else if(referencia.contains("/")){
				String refi = referencia.substring(0,4);
				if(refi.contains("/")){
					for(int i=0 ; i<refi.length(); i++){
						char r = refi.charAt(i);
						String re = String.valueOf(r);
						if(!re.equals("/")){
							if(refencia!=null){
								refencia = refencia.concat(re);
							}else{
								refencia = re;
							}
						}else{
							break;
						}
					}
				}
				ref = Integer.valueOf(refencia!=null ? refencia:refi);
			}else if(referencia.matches("^\\d+$")){
				ref = Integer.valueOf(!referencia.equals("0") ? (referencia.length()>9 ? referencia.substring(referencia.length()-4, referencia.length()):referencia) : formt.format(new Date()) );
				//ref = Integer.valueOf(!referencia.equals("0") ? referencia : formt.format(new Date()));
			}else if(referencia.substring(0,2).equals("P-")){
				ref = Integer.valueOf(referencia.substring(2,referencia.length()));
			}else {
				for(int i=0 ; i<referencia.length(); i++){
					char r = referencia.charAt(i);
					String re = String.valueOf(r);
					if(isNumeric(re)){
						if(refencia!=null){
							refencia = refencia.concat(re);
						}else{
							refencia = re;
						}
					}else{
						break;
					}
				}
				if(refencia!=null){
					ref = Integer.valueOf(refencia);
				}else{
					ref = Integer.valueOf(formt.format(new Date()));
				}
				
			}
		}
		return ref;
	}
	
	private boolean isNumeric(String cadena) {

        boolean resultado;

        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }

	
	/**
	 * metodo que valida su el tipo de movimiento es una factura o un recibo de caja
	 * @param moviTipo
	 * @param opcion
	 * @return
	 * @author emedina
	 */
	private boolean validarNoEsReciboCaja(String moviTipo, int opcion){
		boolean recibo= false;
		switch (opcion) {
		case MOVI_IPS:
			switch (moviTipo) {
					case "MIF":
						recibo = true;
						break;
					case "FI2":
						recibo = true;		
						break;
					case "FI3":
						recibo = true;
						break;
					case "FI4":
						recibo = true;
						break;
					case "FI5":
						recibo = true;
						break;
					case "SAI":
						recibo = true;
						break;
				}
			break;
			case MOVI_EPS:
				try {
					if(moviTipo.equals("CAR")){
						recibo = true;
					}else{
						recibo = false;
					}
						
					
				} catch (Exception e) {
					e.printStackTrace();
					FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_FATAL,"", prop.getProperty("errorConsulTipoDocARS")));
				}
			break;
			case MOVI_NEW_HOTEL:
				try {
					moviTipo = fileMovNehot.getFileName();
					switch (moviTipo.substring(0,3)) {
					case "IMH":
						recibo = true;
						break;
					case "IMK":
						recibo = true;
						break;
					case "IPH":
						recibo = true;
						break;
					case "IPK":
						recibo = true;
						break;
					case "IRH":
						recibo = true;
						break;
					case "IRK":
						recibo = true;
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_FATAL,"", prop.getProperty("errorConsulTipoDocARS")));
				}
				break;
			case MOVI_SCHOOL_PACK:
				try {
					switch (moviTipo) {
					case "FET":
						recibo = true;
						break;
					case "FED":
						recibo = true;
						break;
					case "FES":
						recibo = true;
						break;
					case "FEC":
						recibo = true;
						break;
					case "FEE":
						recibo = true;
						break;
					case "FAM":
						recibo = true;
						break;
					case "NC1":
						recibo = true;
						break;
					case "ND1":
						recibo = true;
						break;
					case "NC5":
						recibo = true;
						break;
					case "ND5":
						recibo = true;
						break;
					case "NC2":
						recibo = true;
						break;
					case "ND2":
						recibo = true;
						break;
					case "NC3":
						recibo = true;
						break;
					case "ND3":
						recibo = true;
						break;
					case "NC4":
						recibo = true;
						break;
					case "ND4":
						recibo = true;
						break;
					case "NC7":
						recibo = true;
						break;
					case "ND7":
						recibo = true;
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_FATAL,"", prop.getProperty("errorConsulTipoDocARS")));
				}
				break;
			case MOVI_CAMPUS:
				try {
					switch (moviTipo) {
					case "FPT":
						recibo = true;
						break;
					case "NC6":
						recibo = true;
						break;
					case "ND6":
						recibo = true;
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_FATAL,"", prop.getProperty("errorConsulTipoDocARS")));
				}
				break;
			case MOVI_SPORT:
				try {
					switch (moviTipo) {
					case "FID":
						recibo = true;
						break;
					case "NC8":
						recibo = true;
						break;
					case "ND8":
						recibo = true;
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					FacesContext.getCurrentInstance().addMessage(null,  new FacesMessage(FacesMessage.SEVERITY_FATAL,"", prop.getProperty("errorConsulTipoDocARS")));
				}
				break;
		}
		
		return recibo;
	}
	
	private boolean validarReciboCaja(String moviTipo){
		boolean recibo= false;
		switch (moviTipo) {
		case "MIR":
			recibo = true;
			break;
		case "RIS":
			recibo = true;
			break;
		case "RIP":
			recibo = true;
			break;
		case "RID":
			recibo = true;
			break;
		case "RIC":
			recibo = true;
			break;
		}
		return recibo;
	}
	
	private boolean validarConsignacionLinea(String moviTipo, int opcion){
		boolean recibo= false;
		
		switch (opcion) {
		case MOVI_SCHOOL_PACK:
			switch (moviTipo) {	
				case "R07":
					recibo = true;
					break;
				case "R08":
					recibo = true;
					break;
				case "R09":
					recibo = true;
					break;
				case "R11":
					recibo = true;
					break;
				case "R15":
					recibo = true;
					break;
				case "R14":
					recibo = true;
					break;
			}
			break;
		case MOVI_CAMPUS:
			switch (moviTipo) {	
				case "R17":
					recibo = true;
					break;
			}
			break;
		case MOVI_SPORT:
			switch (moviTipo) {	
				case "R12":
					recibo = true;
					break;
			}
			break;
		}
		
		return recibo;
	}
	
	private boolean validarArchivoIPSSede(String mov){
		Boolean flag = false;
		switch (mov) {
			case "MIF":
				flag = sede == 1 ? true:false;
				break;
			case "FI2":
				flag = sede == 2 ? true:false;
				break;
			case "FI3":
				flag = sede == 3 ? true:false;
				break;
			case "FI4":
				flag = sede == 5 ? true:false;
				break;
			case "FI5":
				flag = sede == 7 ? true:false;
				break;
			case "SAI":
				flag = (sede == 1 ||  sede == 3 || sede == 2) ? true:false;
				break;
			case "MIR":
				flag = sede == 1 ? true:false;
				break;
			case "RIS":
				flag = sede == 3 ? true:false;
				break;
			case "RIP":
				flag = sede == 7 ? true:false;
				break;
			case "RID":
				flag = sede == 2 ? true:false;
				break;
			case "RIC":
				flag = sede == 5 ? true:false;
				break;
		}

		return flag;
	}
	
	/**
	 * METODO QUE VALIDA LAS FECHAS DE ULTIMO CARGUE DE NEW HOTEL VERSUS AL ACTUAL QUE SE QUEIRE GUARDAR
	 */
	public void validarFechaArchivoNewHotel(){
		try {
			boolean valida = false;
			boolean diasValida = false;
			int diferenciaDia = 0;
			int diferenciaMes = 0;
			int diferenciaAnio = 0;
			String nomArch = fileMovNehot.getFileName().substring(0,3);
			switch (nomArch) {
			case "IMH":
					valida = sedeNh == 6 ? true: false;
				break;
			case "IMK":
				valida = sedeNh == 6 ? true: false;
				break;
			case "IPH":
				valida = sedeNh == 4 ? true: false;
				break;
			case "IPK":
				valida = sedeNh == 4 ? true: false;
				break;
			case "IRH":
				valida = sedeNh == 3 ? true: false;
				break;
			case "IRK":
				valida = sedeNh == 3 ? true: false;
				break;
			}
			
			
			if(valida){
				Date fecha = integracionServicio.fechaCargueNewHotel(sedeNh,6,nomArch);
				SimpleDateFormat formattoa= new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatValida= new SimpleDateFormat("yyyy/MM/dd");
				String fff =  formatValida.format(fechaMov);
				Date fechValidaEntra = formatValida.parse(fff);
				if(fecha!=null && !fecha.equals("")){
					if(fechValidaEntra.compareTo(fecha)==0){
						cerrarNhFec();
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"",prop.getProperty("errorFechaIgual")));
					}else if(fechValidaEntra.after(fecha)){
						SimpleDateFormat format = new SimpleDateFormat("dd");
						Calendar fechaCal = Calendar.getInstance();
						Calendar fechaCalHoy = Calendar.getInstance();
						fechaCal.setTime(fecha);
						fechaCalHoy.setTime(fechaMov);
						int diaUltimoCarge = fechaCal.get(Calendar.DAY_OF_MONTH);
						int diaCargueHoy = fechaCalHoy.get(Calendar.DAY_OF_MONTH);
						int mesUltimoCarge = fechaCal.get(Calendar.MONTH)+1;
						int mesUltimoHoy = fechaCalHoy.get(Calendar.MONTH)+1;
						
						diferenciaDia = diaCargueHoy-diaUltimoCarge;
						
						switch (mesUltimoCarge) {
							case 1:
								if(diaUltimoCarge == 31 && diaCargueHoy == 1){
									diasValida = true;
								}else{
									diasValida = true;;
								}
								break;
							case 2:
								if(diaUltimoCarge == 28 && diaCargueHoy == 1){
									diasValida = true;
								}else if(diaUltimoCarge == 29 && diaCargueHoy == 1){
									diasValida = true;;
								}else{
									diasValida = true;
								}
								break;
							case 3:
								if(diaUltimoCarge == 31 && diaCargueHoy == 1){
									diasValida = true;
								}else{
									diasValida = true;
								}
								break;
							case 4:
								if(diaUltimoCarge == 30 && diaCargueHoy == 1){
									diasValida = true;
								}else{
									diasValida = true;
								}
								break;
							case 5:
								if(diaUltimoCarge == 31 && diaCargueHoy == 1){
									diasValida = true;
								}else{
									diasValida = true;
								}							
								break;
							case 6:
								if(diaUltimoCarge == 30 && diaCargueHoy == 1){
									diasValida = true;
								}else{
									diasValida = true;
								}
								break;
							case 7:
								if(diaUltimoCarge == 31 && diaCargueHoy == 1){
									diasValida = true;
								}else{
									diasValida = true;
								}
								break;
							case 8:
								if(diaUltimoCarge == 31 && diaCargueHoy == 1){
									diasValida = true;
								}else{
									diasValida = true;
								}
								break;
							case 9:
								if(diaUltimoCarge == 30 && diaCargueHoy == 1){
									diasValida = true;
								}else{
									diasValida = true;
								}
								break;
							case 10:
								if(diaUltimoCarge == 31 && diaCargueHoy == 1){
									diasValida = true;
								}else{
									diasValida = true;
								}
								break;
							case 11:
								if(diaUltimoCarge == 30 && diaCargueHoy == 1){
									diasValida = true;
								}else{
									diasValida = true;
								}
								break;
							case 12:
								if(diaUltimoCarge == 31 && diaCargueHoy == 1){
									diasValida = true;
								}else{
									diasValida = true;
								}
								break;
						}
						
						
						if(diferenciaDia>=1){
							dialConfNh = false;
							dialConfFecNh = true;
							verificacion = "LOS REGISTROS ALMACENADOS SON DE LA FECHA: "+formattoa.format(fecha)+" Y LOS REGISTROS QUE DESEA ALMACENAR SON DE LA FECHA: "+formattoa.format(fechaMov) 
									+"; CORRESPONDIENTES A: "+diferenciaDia +" DÍAS DE DIFERENCIA";
							verificacionDos = "¿DESEA ALMACENAR LOS REGISTROS?";
						}else if(diasValida){
							dialConfNh = false;
							dialConfFecNh = true;
							verificacion = "LOS REGISTROS ALMACENADOS SON DE LA FECHA: " + formattoa.format(fecha) + " Y LOS REGISTROS QUE DESEA ALMACENAR SON DE LA FECHA: " + formattoa.format(fechaMov);
							verificacionDos = "¿DESEA ALMACENAR LOS REGISTROS?";
						}else if(mesUltimoCarge==12 && mesUltimoHoy==1){
							dialConfNh = false;
							dialConfFecNh = true;
							verificacion = "LOS REGISTROS ALMACENADOS SON DE LA FECHA: " + formattoa.format(fecha) + " Y LOS REGISTROS QUE DESEA ALMACENAR SON DE LA FECHA: " + formattoa.format(fechaMov);
							verificacionDos = "¿DESEA ALMACENAR LOS REGISTROS?";
						}else{
							cerrarNhFec();
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorFechaMenor")));
						}
						
					}else if(fechValidaEntra.before(fecha)){
						dialConfNh = false;
						dialConfFecNh = true;
						verificacion = "LOS REGISTROS ALMACENADOS SON DE LA FECHA: "+formattoa.format(fecha)+" Y LOS REGISTROS QUE DESEA ALMACENAR SON DE LA FECHA: "+formattoa.format(fechaMov) 
								+"; CORRESPONDIENTES A: "+diferenciaDia +" DÍAS DE DIFERENCIA";
						verificacionDos = "¿DESEA ALMACENAR LOS REGISTROS?";
						
//						cerrarNhFec();
//						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorFechaMenor")));
					}
				}else{
					dialConfNh = false;	
					dialConfFecNh = true;
					verificacion = "NO EXISTEN REGISTROS CARGADOS EN EL HISTORIAL, SE ALMACENARA LOS REGISTROS CON LA FECHA DE HOY: "+formattoa.format(new Date());
					verificacionDos = "¿DESEA ALMACENAR LOS REGISTROS?";
				}
			}else{
				cerrarNhFec();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"",prop.getProperty("errorArchSede")));
			}
			
		} catch (Exception e) {
			cerrarNhFec();
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"",prop.getProperty("errorFatalFecha")));
		}
	}
	
	/**
	 * metodo que cierra el paneL
	 */
	public void cerrarNhFec(){
		dialConfFecNh = false;
		cerrarNh();
	}
	
	/**
	 * metodo que cierra el panel
	 * @author emedina 
	 */
	public void cerrar(){
		dialConfM = false;
		processMovi = false;
		btnMovIPS = false;
		btnMovIPSConf = true;
		estruturaArchivoDTOs.clear();
		fileMovIps = null;
		sede = 0;
	}
	
	/**
	 * metodo que cierra el panel
	 * @author emedina 
	 */
	public void cerrarEPS(){
		dialConfMEps = false;
		processMovi = false;
		btnMovEPS = false;
		btnMovEPSConf = true;
		estruturaArchivoDTOs.clear();
		sedeEps = 0;
	}

	/**
	 * metodo que cierra el panel
	 * @author emedina 
	 */
	public void cerrarNh(){
		dialConfNh = false;
		processMovi = false;
		btnMovNh = false;
		btnMovNhConf = true;
		estruturaArchivoDTOs.clear();
		fileMovNehot = null;
		sedeNh = 0;
	}
	
	
	/**
	 * metodo que cierra el panel
	 * @author emedina 
	 */
	public void cerrarCampus(){
		dialConfMCampus = false;
		processMovi = false;
		btnMovCampus = false;
		btnMovCampusConf = true;
		estruturaArchivoDTOs.clear();
		sedeC = 0;
	}
	
	/**
	 * metodo que cierra el panel
	 * @author emedina 
	 */
	public void cerrarSchool(){
		dialConfFecSchool = false;
		btnMovSchool = false;
		btnMovSchoolConf = true;
		processMovi = false;
		estruturaArchivoDTOs.clear();
		sedeSP = 0;
	}
	
	/**
	 * metodo que cierra el panel
	 * @author emedina 
	 */
	public void cerrarSport(){
		dialConfSport = false;
		btnMovSport = false;
		btnMovSportConf = true;
		processMovi = false;
		estruturaArchivoDTOs.clear();
		sedeS = 0;
	}

	
	/**
	 * metodo que realiza confirmacón del guardado de datos
	 * @author emedina
	 */
	public void confirmacion(){
		dialConfM = true;
	}	
	
	/**
	 * metodo que realiza confirmacón del guardado de datos EPS
	 * @author emedina
	 */
	public void confirmacionEps(){
		dialConfMEps = true;
	}
	
	
	/**
	 * metodo que realiza confirmacón del guardado de datos EPS
	 * @author emedina
	 */
	public void confirmacionNh(){
		dialConfNh = true;
	}
	
	/**
	 * metodo que realiza confirmacón del guardado de datos EPS
	 * @author emedina
	 */
	public void confirmacionSchool(){
		dialConfFecSchool = true;
	}
	
	/**
	 * metodo que realiza confirmacón del guardado de datos EPS
	 * @author emedina
	 */
	public void confirmacionSport(){
		dialConfSport= true;
	}
	
	/**
	 * metodo que realiza confirmacón del guardado de datos EPS
	 * @author emedina
	 */
	public void confirmacionCampus(){
		dialConfMCampus = true;
	}
	
	/*
	 * metodos set y get
	 */
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public UploadedFile getFileMovIps() {
		return fileMovIps;
	}

	public void setFileMovIps(UploadedFile fileMovIps) {
		this.fileMovIps = fileMovIps;
	}

	public UploadedFile getFileMovEps() {
		return fileMovEps;
	}

	public void setFileMovEps(UploadedFile fileMovEps) {
		this.fileMovEps = fileMovEps;
	}

	public UploadedFile getFileMovNehot() {
		return fileMovNehot;
	}

	public void setFileMovNehot(UploadedFile fileMovNehot) {
		this.fileMovNehot = fileMovNehot;
	}

	public List<String> getErrores() {
		return errores;
	}

	public void setErrores(List<String> errores) {
		this.errores = errores;
	}

	public List<String> getDatosCarga() {
		return datosCarga;
	}

	public void setDatosCarga(List<String> datosCarga) {
		this.datosCarga = datosCarga;
	}

	public boolean isShowErrores() {
		return showErrores;
	}

	public void setShowErrores(boolean showErrores) {
		this.showErrores = showErrores;
	}

	public List<EstructuraArchMoviDTO> getEstruturaArchivoDTOs() {
		return estruturaArchivoDTOs;
	}

	public void setEstruturaArchivoDTOs(List<EstructuraArchMoviDTO> estruturaArchivoDTOs) {
		this.estruturaArchivoDTOs = estruturaArchivoDTOs;
	}

	public boolean isDialConfM() {
		return dialConfM;
	}

	public void setDialConfM(boolean dialConfM) {
		this.dialConfM = dialConfM;
	}

	public boolean isBtnMovIPS() {
		return btnMovIPS;
	}

	public void setBtnMovIPS(boolean btnMovIPS) {
		this.btnMovIPS = btnMovIPS;
	}

	public boolean isBtnMovIPSConf() {
		return btnMovIPSConf;
	}

	public void setBtnMovIPSConf(boolean btnMovIPSConf) {
		this.btnMovIPSConf = btnMovIPSConf;
	}

	public List<String> getErroresMovi() {
		return erroresMovi;
	}

	public void setErroresMovi(List<String> erroresMovi) {
		this.erroresMovi = erroresMovi;
	}

	public boolean isShowErroresMovi() {
		return showErroresMovi;
	}

	public void setShowErroresMovi(boolean showErroresMovi) {
		this.showErroresMovi = showErroresMovi;
	}

	public Date getFecIni() {
		return fecIni;
	}

	public void setFecIni(Date fecIni) {
		this.fecIni = fecIni;
	}

	public Date getFecFin() {
		return fecFin;
	}

	public void setFecFin(Date fecFin) {
		this.fecFin = fecFin;
	}

	public Integer getSede() {
		return sede;
	}

	public void setSede(Integer sede) {
		this.sede = sede;
	}

	public List<Tinmsede> getListSede() {
		return listSede;
	}

	public void setListSede(List<Tinmsede> listSede) {
		this.listSede = listSede;
	}

	public Integer getSedeEps() {
		return sedeEps;
	}

	public void setSedeEps(Integer sedeEps) {
		this.sedeEps = sedeEps;
	}

	public boolean isBtnMovEPS() {
		return btnMovEPS;
	}

	public void setBtnMovEPS(boolean btnMovEPS) {
		this.btnMovEPS = btnMovEPS;
	}

	public boolean isBtnMovEPSConf() {
		return btnMovEPSConf;
	}

	public void setBtnMovEPSConf(boolean btnMovEPSConf) {
		this.btnMovEPSConf = btnMovEPSConf;
	}

	public boolean isDialConfMEps() {
		return dialConfMEps;
	}

	public void setDialConfMEps(boolean dialConfMEps) {
		this.dialConfMEps = dialConfMEps;
	}

//	public List<Tinddecl> getTinddecls() {
//		return tinddecls;
//	}
//
//	public void setTinddecls(List<Tinddecl> tinddecls) {
//		this.tinddecls = tinddecls;
//	}
//
//	public List<Tinddede> getTinddedes() {
//		return tinddedes;
//	}
//
//	public void setTinddedes(List<Tinddede> tinddedes) {
//		this.tinddedes = tinddedes;
//	}

	public boolean isBtnMovNh() {
		return btnMovNh;
	}

	public void setBtnMovNh(boolean btnMovNh) {
		this.btnMovNh = btnMovNh;
	}

	public boolean isBtnMovNhConf() {
		return btnMovNhConf;
	}

	public void setBtnMovNhConf(boolean btnMovNhConf) {
		this.btnMovNhConf = btnMovNhConf;
	}

	public boolean isDialConfNh() {
		return dialConfNh;
	}

	public void setDialConfNh(boolean dialConfNh) {
		this.dialConfNh = dialConfNh;
	}

	public Integer getSedeNh() {
		return sedeNh;
	}

	public void setSedeNh(Integer sedeNh) {
		this.sedeNh = sedeNh;
	}

	public boolean isShowErroresNh() {
		return showErroresNh;
	}

	public void setShowErroresNh(boolean showErroresNh) {
		this.showErroresNh = showErroresNh;
	}

	public boolean isDialConfFecNh() {
		return dialConfFecNh;
	}

	public void setDialConfFecNh(boolean dialConfFecNh) {
		this.dialConfFecNh = dialConfFecNh;
	}

	public String getVerificacion() {
		return verificacion;
	}

	public void setVerificacion(String verificacion) {
		this.verificacion = verificacion;
	}

	public String getVerificacionDos() {
		return verificacionDos;
	}

	public void setVerificacionDos(String verificacionDos) {
		this.verificacionDos = verificacionDos;
	}

	public Date getFecIniSP() {
		return fecIniSP;
	}

	public void setFecIniSP(Date fecIniSP) {
		this.fecIniSP = fecIniSP;
	}

	public Date getFecFinSP() {
		return fecFinSP;
	}

	public void setFecFinSP(Date fecFinSP) {
		this.fecFinSP = fecFinSP;
	}

	public Date getFecIniSt() {
		return fecIniSt;
	}

	public void setFecIniSt(Date fecIniSt) {
		this.fecIniSt = fecIniSt;
	}

	public Date getFecFinSt() {
		return fecFinSt;
	}

	public void setFecFinSt(Date fecFinSt) {
		this.fecFinSt = fecFinSt;
	}

	public Date getFecIniC() {
		return fecIniC;
	}

	public void setFecIniC(Date fecIniC) {
		this.fecIniC = fecIniC;
	}

	public Date getFecFinC() {
		return fecFinC;
	}

	public void setFecFinC(Date fecFinC) {
		this.fecFinC = fecFinC;
	}

	public Integer getSedeSP() {
		return sedeSP;
	}

	public void setSedeSP(Integer sedeSP) {
		this.sedeSP = sedeSP;
	}

	public Integer getSedeS() {
		return sedeS;
	}

	public void setSedeS(Integer sedeS) {
		this.sedeS = sedeS;
	}

	public Integer getSedeC() {
		return sedeC;
	}

	public void setSedeC(Integer sedeC) {
		this.sedeC = sedeC;
	}

	public boolean isBtnMovCampus() {
		return btnMovCampus;
	}

	public void setBtnMovCampus(boolean btnMovCampus) {
		this.btnMovCampus = btnMovCampus;
	}

	public boolean isBtnMovCampusConf() {
		return btnMovCampusConf;
	}

	public void setBtnMovCampusConf(boolean btnMovCampusConf) {
		this.btnMovCampusConf = btnMovCampusConf;
	}

	public boolean isDialConfMCampus() {
		return dialConfMCampus;
	}

	public void setDialConfMCampus(boolean dialConfMCampus) {
		this.dialConfMCampus = dialConfMCampus;
	}

	public boolean isBtnMovSchool() {
		return btnMovSchool;
	}

	public void setBtnMovSchool(boolean btnMovSchool) {
		this.btnMovSchool = btnMovSchool;
	}

	public boolean isBtnMovSchoolConf() {
		return btnMovSchoolConf;
	}

	public void setBtnMovSchoolConf(boolean btnMovSchoolConf) {
		this.btnMovSchoolConf = btnMovSchoolConf;
	}

	public boolean isDialConfFecSchool() {
		return dialConfFecSchool;
	}

	public void setDialConfFecSchool(boolean dialConfFecSchool) {
		this.dialConfFecSchool = dialConfFecSchool;
	}

	public boolean isDialConfSport() {
		return dialConfSport;
	}

	public void setDialConfSport(boolean dialConfSport) {
		this.dialConfSport = dialConfSport;
	}

	public boolean isBtnMovSport() {
		return btnMovSport;
	}

	public void setBtnMovSport(boolean btnMovSport) {
		this.btnMovSport = btnMovSport;
	}

	public boolean isBtnMovSportConf() {
		return btnMovSportConf;
	}

	public void setBtnMovSportConf(boolean btnMovSportConf) {
		this.btnMovSportConf = btnMovSportConf;
	}

	public List<Tinmcxp> getListTinmcxp() {
		return listTinmcxp;
	}

	public void setListTinmcxp(List<Tinmcxp> listTinmcxp) {
		this.listTinmcxp = listTinmcxp;
	}

	public Integer getTipoMovIPS() {
		return tipoMovIPS;
	}

	public void setTipoMovIPS(Integer tipoMovIPS) {
		this.tipoMovIPS = tipoMovIPS;
	}
	
	
}
