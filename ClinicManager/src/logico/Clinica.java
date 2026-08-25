package logico;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class Clinica implements Serializable {
	private static final long serialVersionUID = 8332914665293201379L;
	private static Clinica instancia = null;
	private ArrayList<Paciente> pacientes = new ArrayList<>();
	private ArrayList<Medico> medicos = new ArrayList<>();
	private ArrayList<Cita> citas = new ArrayList<>();
	private ArrayList<Consulta> consultas = new ArrayList<>();
	private ArrayList<Vacuna> catalogoVacunas = new ArrayList<>();
	private ArrayList<EnfermedadBajoVigilancia> enfermedadesVigiladas = new ArrayList<>();
	private ArrayList<Departamento> departamentos = new ArrayList<>();
	private ArrayList<SeguroMedico> seguros = new ArrayList<>();
	private ArrayList<PacienteVacuna> pacienteVacunas = new ArrayList<>();
	private ArrayList<Medicamento> medicamentos = new ArrayList<>();
	private ArrayList<Receta> recetas = new ArrayList<>();
	private ArrayList<ExamenLaboratorio> catalogoExamenes = new ArrayList<>();
	private ArrayList<OrdenExamen> ordenesExamen = new ArrayList<>();
	private ArrayList<Factura> facturas = new ArrayList<>();
	private ArrayList<Secretaria> secretarias = new ArrayList<>();

	private int contadorPacientes = 1;
	private int contadorMedicos = 1;
	private int contadorConsultas = 1;
	private int contadorVacunas = 1;
	private int contadorEnfermedades = 1;
	private int contadorCitas = 1;
	private int contadorDepartamentos = 1;
	private int contadorSeguros = 1;
	private int contadorPacienteVacunas = 1;
	private int contadorMedicamentos = 1;
	private int contadorRecetas = 1;
	private int contadorExamenes = 1;
	private int contadorOrdenes = 1;
	private int contadorFacturas = 1;

	private Clinica() {
	}

	public static Clinica getInstancia() {
		if (instancia == null) {
			instancia = new Clinica();
		}
		if (instancia.getCatalogoVacunas() == null || instancia.getCatalogoVacunas().isEmpty()) {
			if (instancia.getCatalogoVacunas() == null) {
				instancia.setCatalogoVacunas(new ArrayList<Vacuna>());
			}
			instancia.agregarVacuna("COVID-19", "Pfizer", 2.0f, "Vacuna MRNA SARS-COV-2");
			instancia.agregarVacuna("Influenza", "Generico", 1.0f, "Vacuna anual");
			instancia.agregarVacuna("Tétanos", "Generico", 1.0f, "Refuerzo cada 10 años");
			instancia.agregarVacuna("Hepatitis B", "Generico", 3.0f, "Vacuna serie VHB");
		}
		if (instancia.getSeguros() == null || instancia.getSeguros().isEmpty()) {
			if (instancia.getSeguros() == null) {
				instancia.setSeguros(new ArrayList<SeguroMedico>());
			}
			instancia.agregarSeguro("Senasa", "430192312", "809-555-1234", 100f);
			instancia.agregarSeguro("Humano", "430192313", "809-555-1235", 85f);
			instancia.agregarSeguro("Palic", "430192314", "809-555-1236", 80f);
			instancia.agregarSeguro("Universal", "430192315", "809-555-1237", 80f);
		}
		if (instancia.getMedicos() == null || instancia.getMedicos().isEmpty()) {
			if (instancia.getMedicos() == null) {
				instancia.setMedicos(new ArrayList<Medico>());
			}
			instancia.agregarMedico("Juan", "Perez (Default)", 45, "001-1234567-8", "Masculino", "Medicina General",
					15);
		}
		return instancia;
	}

	public static void setInstancia(Clinica c) {
		instancia = c;
	}

	public Paciente agregarPaciente(String nombre, String apellido, int edad, String cedula,
			String sexo, float peso, float estatura, String tipoSangre, String direccion, String telefono) {
		Paciente paciente = new Paciente("PAC" + contadorPacientes, nombre, apellido, edad, cedula, sexo, peso,
				estatura, tipoSangre, direccion, telefono);
		pacientes.add(paciente);
		contadorPacientes++;
		return paciente;

	}

	public Medico agregarMedico(String nombre, String apellido, int edad, String cedula, String sexo,
			String especialidad, int maxCitas) {
		Medico medico = new Medico("MED" + contadorMedicos, nombre, apellido, edad, cedula, sexo, especialidad,
				maxCitas);
		medicos.add(medico);
		contadorMedicos++;
		return medico;
	}

	public void agregarCita(String nombre, String apellido, String cedula, Medico medico, LocalDate fecha) {
		String id_paciente = buscarPacientePorCedula(cedula);
		Cita cita = new Cita(id_paciente, medico.getId(), fecha);
		citas.add(cita);
		contadorCitas++;
		dao.CitaDAO.registrarCita(cita);
	}

	public void agregarCita(Paciente paciente, Medico medico, LocalDate fecha) {
		Cita cita = new Cita(paciente.getId(), medico.getId(), fecha);
		citas.add(cita);
		contadorCitas++;
		dao.CitaDAO.registrarCita(cita);
	}

	public Consulta agregarConsulta(Paciente paciente, Medico medico, String sintomas, String diagnostico,
			EnfermedadBajoVigilancia enfermedadVigilada, boolean esImportante) {
		Consulta consulta = new Consulta("CON" + contadorConsultas, paciente, medico, sintomas, diagnostico,
				enfermedadVigilada,
				esImportante);
		consultas.add(consulta);
		contadorConsultas++;
		return consulta;

	}

	public void agregarEnfermedadVigilida(String nombre, String descripcion, String gravedad) {
		EnfermedadBajoVigilancia enfermedadVigilada = new EnfermedadBajoVigilancia("ENF" + contadorEnfermedades, nombre,
				descripcion, gravedad);
		enfermedadesVigiladas.add(enfermedadVigilada);
		contadorEnfermedades++;

	}

	public void agregarVacuna(String nombre, String fabricante, float dosis, String descripcion) {
		Vacuna vacuna = new Vacuna("VAC" + contadorVacunas, nombre, fabricante, dosis, descripcion);
		catalogoVacunas.add(vacuna);
		contadorVacunas++;
	}

	public Departamento agregarDepartamento(String nombre, String descripcion, String ubicacion) {
		Departamento dep = new Departamento("DEP" + contadorDepartamentos, nombre, descripcion, ubicacion);
		departamentos.add(dep);
		contadorDepartamentos++;
		return dep;
	}

	public SeguroMedico agregarSeguro(String nombre, String rnc, String telefono, float porcentajeCobertura) {
		SeguroMedico seguro = new SeguroMedico("SEG" + contadorSeguros, nombre, rnc, telefono, porcentajeCobertura);
		seguros.add(seguro);
		contadorSeguros++;
		return seguro;
	}

	public PacienteVacuna agregarPacienteVacuna(Paciente paciente, Vacuna vacuna, java.time.LocalDate fechaAplicacion,
			String lote, int numeroDosis) {
		PacienteVacuna pv = new PacienteVacuna("PV" + contadorPacienteVacunas, paciente, vacuna, fechaAplicacion, lote,
				numeroDosis);
		pacienteVacunas.add(pv);
		contadorPacienteVacunas++;
		return pv;
	}

	public Medicamento agregarMedicamento(String nombre, String principioActivo, String presentacion,
			String concentracion) {
		Medicamento med = new Medicamento("MEDF" + contadorMedicamentos, nombre, principioActivo, presentacion,
				concentracion);
		medicamentos.add(med);
		contadorMedicamentos++;
		return med;
	}

	public Receta agregarReceta(Consulta consulta, java.time.LocalDate fecha,
			String observaciones) {
		Receta receta = new Receta("REC" + contadorRecetas, consulta, fecha, observaciones);
		recetas.add(receta);
		contadorRecetas++;
		return receta;
	}

	public ExamenLaboratorio agregarExamenLaboratorio(String nombre, String descripcion, String categoria,
			float precioBase) {
		ExamenLaboratorio examen = new ExamenLaboratorio("EXA" + contadorExamenes, nombre, descripcion, categoria,
				precioBase);
		catalogoExamenes.add(examen);
		contadorExamenes++;
		return examen;
	}

	public OrdenExamen agregarOrdenExamen(Consulta consulta, ExamenLaboratorio examen,
			java.time.LocalDate fechaOrden) {
		OrdenExamen orden = new OrdenExamen("ORD" + contadorOrdenes, consulta, examen, fechaOrden);
		ordenesExamen.add(orden);
		contadorOrdenes++;
		return orden;
	}

	public Factura agregarFactura(Consulta consulta, SeguroMedico seguro,
			java.time.LocalDate fecha) {
		Factura factura = new Factura("FAC" + contadorFacturas, consulta, seguro, fecha);
		facturas.add(factura);
		contadorFacturas++;
		return factura;
	}

	public ArrayList<Paciente> getPacientes() {
		return pacientes;
	}

	public void setPacientes(ArrayList<Paciente> pacientes) {
		this.pacientes = pacientes;
	}

	public ArrayList<Medico> getMedicos() {
		return medicos;
	}

	public void setMedicos(ArrayList<Medico> medicos) {
		this.medicos = medicos;
	}

	public ArrayList<Cita> getCitas() {
		return citas;
	}

	public void setCitas(ArrayList<Cita> citas) {
		this.citas = citas;
	}

	public ArrayList<Consulta> getConsultas() {
		return consultas;
	}

	public void setConsultas(ArrayList<Consulta> consultas) {
		this.consultas = consultas;
	}

	public ArrayList<Vacuna> getCatalogoVacunas() {
		return catalogoVacunas;
	}

	public void setCatalogoVacunas(ArrayList<Vacuna> catalogoVacunas) {
		this.catalogoVacunas = catalogoVacunas;
	}

	public ArrayList<EnfermedadBajoVigilancia> getEnfermedadesVigiladas() {
		return enfermedadesVigiladas;
	}

	public void setEnfermedadesVigiladas(ArrayList<EnfermedadBajoVigilancia> enfermedadesVigiladas) {
		this.enfermedadesVigiladas = enfermedadesVigiladas;
	}

	public ArrayList<Departamento> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(ArrayList<Departamento> departamentos) {
		this.departamentos = departamentos;
	}

	public ArrayList<SeguroMedico> getSeguros() {
		return seguros;
	}

	public void setSeguros(ArrayList<SeguroMedico> seguros) {
		this.seguros = seguros;
	}

	public ArrayList<PacienteVacuna> getPacienteVacunas() {
		return pacienteVacunas;
	}

	public void setPacienteVacunas(ArrayList<PacienteVacuna> pacienteVacunas) {
		this.pacienteVacunas = pacienteVacunas;
	}

	public ArrayList<Medicamento> getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(ArrayList<Medicamento> medicamentos) {
		this.medicamentos = medicamentos;
	}

	public ArrayList<Receta> getRecetas() {
		return recetas;
	}

	public void setRecetas(ArrayList<Receta> recetas) {
		this.recetas = recetas;
	}

	public ArrayList<ExamenLaboratorio> getCatalogoExamenes() {
		return catalogoExamenes;
	}

	public void setCatalogoExamenes(ArrayList<ExamenLaboratorio> catalogoExamenes) {
		this.catalogoExamenes = catalogoExamenes;
	}

	public ArrayList<OrdenExamen> getOrdenesExamen() {
		return ordenesExamen;
	}

	public void setOrdenesExamen(ArrayList<OrdenExamen> ordenesExamen) {
		this.ordenesExamen = ordenesExamen;
	}

	public ArrayList<Factura> getFacturas() {
		return facturas;
	}

	public void setFacturas(ArrayList<Factura> facturas) {
		this.facturas = facturas;
	}

	public Departamento buscarDepartamentoPorId(String id) {
		if (id == null || departamentos == null)
			return null;
		for (Departamento d : departamentos) {
			if (id.equalsIgnoreCase(d.getId()))
				return d;
		}
		return null;
	}

	public SeguroMedico buscarSeguroPorId(String id) {
		if (id == null || seguros == null)
			return null;
		for (SeguroMedico s : seguros) {
			if (id.equalsIgnoreCase(s.getId()))
				return s;
		}
		return null;
	}

	public Medicamento buscarMedicamentoPorId(String id) {
		if (id == null || medicamentos == null)
			return null;
		for (Medicamento m : medicamentos) {
			if (id.equalsIgnoreCase(m.getId()))
				return m;
		}
		return null;
	}

	public ExamenLaboratorio buscarExamenPorId(String id) {
		if (id == null || catalogoExamenes == null)
			return null;
		for (ExamenLaboratorio e : catalogoExamenes) {
			if (id.equalsIgnoreCase(e.getId()))
				return e;
		}
		return null;
	}

	public Vacuna buscarVacunaPorId(String id) {
		if (id == null || catalogoVacunas == null) {
			return null;
		}

		for (Vacuna v : catalogoVacunas) {
			if (id.equalsIgnoreCase(v.getId())) {
				return v;
			}
		}

		return null;

	}

	public EnfermedadBajoVigilancia buscarEnfermedadPorId(String id) {
		if (id == null || enfermedadesVigiladas == null) {
			return null;
		}

		for (EnfermedadBajoVigilancia e : enfermedadesVigiladas) {
			if (id.equalsIgnoreCase(e.getId())) {
				return e;
			}
		}

		return null;

	}

	public Medico buscarMedicoPorId(String id) {
		if (id == null || medicos == null) {
			return null;
		}

		for (Medico m : medicos) {
			if (id.equalsIgnoreCase(m.getId())) {
				return m;
			}
		}

		return null;

	}

	public Paciente buscarPacientePorId(String id) {
		if (id == null || pacientes == null) {
			return null;
		}

		for (Paciente p : pacientes) {
			if (id.equalsIgnoreCase(p.getId())) {
				return p;
			}
		}

		return null;

	}

	public boolean verificarCedula(String cedula) {
		for (Medico m : medicos) {
			if (cedula.equalsIgnoreCase(m.getCedula())) {
				return true;
			}

		}

		for (Paciente p : pacientes) {
			if (cedula.equalsIgnoreCase(p.getCedula())) {
				return true;
			}
		}

		return false;

	}

	public ArrayList<Medico> getMedicosDisponibles(LocalDate fecha) {
		ArrayList<Medico> medicosDisponibles = new ArrayList<>();
		for (Medico m : dao.MedicoDAO.obtenerMedicosParaConsultas()) {
			if (m == null || !m.isActivo()) {
				continue;
			}

			int contador = dao.CitaDAO.contarCitasActivasPorMedico(m.getId(), fecha);

			if (contador < m.getMaxCitas()) {
				medicosDisponibles.add(m);
			}
		}

		return medicosDisponibles;

	}

	public boolean medicoPuedeAceptarCita(String medicoId, LocalDate fecha) {
		boolean puedeAceptar = false;
		ArrayList<Medico> medicos = getMedicosDisponibles(fecha);

		for (Medico c : medicos) {
			if (c.getId().equalsIgnoreCase(medicoId)) {
				puedeAceptar = true;
				return puedeAceptar;
			}
		}

		return puedeAceptar;
	}

	public Cita buscarCitaPorId(String CitaId) {
		for (Cita c : citas) {
			if (c.getId().equalsIgnoreCase(CitaId)) {
				return c;
			}
		}

		return null;
	}

	public String buscarPacientePorCedula(String cedula) {

		for (Paciente p : pacientes) {
			if (p.getCedula().equalsIgnoreCase(cedula)) {
				return p.getId();
			}
		}

		return null;
	}

	public boolean verificarSiPacienteExiste(String cedula) {

		for (Paciente p : pacientes) {
			if (p.getCedula().equalsIgnoreCase(cedula)) {
				return true;
			}
		}

		return false;
	}

	public boolean verificarVacunaRepetida(Paciente paciente, String vacunaId) {
		if (paciente == null || vacunaId == null)
			return false;
		ArrayList<Vacuna> vacunasPaciente = paciente.getVacunas();
		if (vacunasPaciente == null)
			return false;
		for (Vacuna v : vacunasPaciente) {
			if (v != null && vacunaId.equalsIgnoreCase(v.getId())) {
				return true;
			}
		}
		return false;
	}

	public boolean guardarRespaldo() {
		try {
			File carpetaRespaldos = new File("respaldos");
			if (!carpetaRespaldos.exists()) {
				carpetaRespaldos.mkdirs();
			}

			String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
			String nombreArchivo = "respaldo_" + timestamp + ".dat";
			File archivoRespaldo = new File(carpetaRespaldos, nombreArchivo);

			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoRespaldo))) {
				oos.writeObject(this);
				System.out.println("Respaldo guardado: " + nombreArchivo);
				return true;
			}
		} catch (IOException e) {
			System.err.println("Error guardando respaldo: " + e.getMessage());
			return false;
		}
	}

	public boolean restaurarRespaldo(String nombreRespaldo) {
		try {
			File carpetaRespaldos = new File("respaldos");
			File archivoRespaldo = new File(carpetaRespaldos, nombreRespaldo);

			if (!archivoRespaldo.exists()) {
				System.err.println("Archivo de respaldo no encontrado: " + nombreRespaldo);
				return false;
			}

			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoRespaldo))) {
				Clinica clinicaRestaurada = (Clinica) ois.readObject();

				this.pacientes = clinicaRestaurada.pacientes;
				this.medicos = clinicaRestaurada.medicos;
				this.citas = clinicaRestaurada.citas;
				this.consultas = clinicaRestaurada.consultas;
				this.catalogoVacunas = clinicaRestaurada.catalogoVacunas;
				this.enfermedadesVigiladas = clinicaRestaurada.enfermedadesVigiladas;

				this.contadorPacientes = clinicaRestaurada.contadorPacientes;
				this.contadorMedicos = clinicaRestaurada.contadorMedicos;
				this.contadorConsultas = clinicaRestaurada.contadorConsultas;
				this.contadorVacunas = clinicaRestaurada.contadorVacunas;
				this.contadorEnfermedades = clinicaRestaurada.contadorEnfermedades;
				this.contadorCitas = clinicaRestaurada.contadorCitas;

				System.out.println("Respaldo restaurado: " + nombreRespaldo);
				return true;
			}
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error restaurando respaldo: " + e.getMessage());
			return false;
		}
	}

	public ArrayList<String> listarRespaldos() {
		ArrayList<String> respaldos = new ArrayList<>();
		File carpetaRespaldos = new File("respaldos");

		if (carpetaRespaldos.exists() && carpetaRespaldos.isDirectory()) {
			File[] archivos = carpetaRespaldos
					.listFiles((dir, name) -> name.startsWith("respaldo_") && name.endsWith(".dat"));

			if (archivos != null) {
				for (File archivo : archivos) {
					respaldos.add(archivo.getName());
				}
			}
		}
		return respaldos;
	}

	public ArrayList<Secretaria> getSecretarias() {
		return secretarias;
	}

	public void insertarSecretaria(Secretaria s) {
		if (s != null) {
			secretarias.add(s);
		}
	}
}