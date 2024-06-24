package com.entidades.buenSabor.business.facade.Imp;

import com.entidades.buenSabor.business.facade.Base.BaseFacadeImp;
import com.entidades.buenSabor.business.facade.PedidoFacade;
import com.entidades.buenSabor.business.mapper.BaseMapper;
import com.entidades.buenSabor.business.service.Base.BaseService;
import com.entidades.buenSabor.business.service.PedidoService;
import com.entidades.buenSabor.domain.dto.pedido.PedidoFullDto;
import com.entidades.buenSabor.domain.entities.Articulo;
import com.entidades.buenSabor.domain.entities.Cliente;
import com.entidades.buenSabor.domain.entities.DetallePedido;
import com.entidades.buenSabor.domain.entities.Pedido;
import com.entidades.buenSabor.domain.enums.Estado;
import com.entidades.buenSabor.domain.enums.Rol;
import com.entidades.buenSabor.repositories.PedidoRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PedidoFacadeImp extends BaseFacadeImp<Pedido, PedidoFullDto, Long> implements PedidoFacade {
    @Autowired

    private PedidoService pedidoService;

    public List<PedidoFullDto> findByClienteId(Long idCliente) {
        return this.pedidoService.findByClienteId(idCliente);
    }

    public List<PedidoFullDto> pedidosSucursal(Long idSucursal) {
        return this.pedidoService.pedidosSucursal(idSucursal);
    }

    public PedidoFacadeImp(BaseService<Pedido, Long> baseService, BaseMapper<Pedido, PedidoFullDto> baseMapper, PedidoService pedidoService) {
        super(baseService, baseMapper);
        this.pedidoService = pedidoService;
    }

    public List<Object[]> getRankingInsumoData(Long sucursalId) {
        return this.pedidoService.getRankingInsumo(sucursalId)
                .stream().map((value) -> new Object[]{((Articulo) value[0]).getDenominacion(), value[1]})
                .toList();
    }

    public SXSSFWorkbook getRankingInsumo(Long sucursalId, Instant desde, Instant hasta) {

        // Se crea el libro
        SXSSFWorkbook libro = new SXSSFWorkbook(50); //importante !! el 50 indica el tamaño de paginación
        // Se crea una hoja dentro del libro
        SXSSFSheet hoja = libro.createSheet();
        //estilo
        XSSFFont font = (XSSFFont) libro.createFont();
        font.setBold(true);
        XSSFCellStyle style = (XSSFCellStyle) libro.createCellStyle();
        style.setFont(font);
        int nroColumna = 0;
        // Se crea una fila dentro de la hoja
        SXSSFRow row = hoja.createRow(0);
        // Se crea una celda dentro de la fila
        SXSSFCell cell = row.createCell(nroColumna);
        cell.setCellValue("Articulo");
        cell.setCellStyle(style);
        cell = row.createCell(++nroColumna);
        cell.setCellValue("Cantidad");
        cell.setCellStyle(style);

        int nroFila = 1;
        nroColumna = 0;
        List<Object[]> rankingArticulos = this.pedidoService.getRankingInsumo(sucursalId, desde, hasta);
        if (rankingArticulos != null) {
            for (Object[] object : rankingArticulos) {
                nroColumna = 0;
                row = hoja.createRow(nroFila);
                ++nroFila;
                cell = row.createCell(nroColumna);
                cell.setCellValue(((Articulo) object[0]).getDenominacion());
                cell = row.createCell(++nroColumna);
                cell.setCellValue(((Long) object[1]));
            }
        }
        return libro;
    }

    public List<Object[]> getCantidadDePedidosPorData(Long sucursalId) {
        return this.pedidoService.getCantidadPedidosPorCliente(sucursalId);
    }

    public SXSSFWorkbook getCantidadDePedidosPorCliente(Long sucursalId, Instant desde, Instant hasta) {

        // Se crea el libro
        SXSSFWorkbook libro = new SXSSFWorkbook(50); //importante !! el 50 indica el tamaño de paginación
        // Se crea una hoja dentro del libro
        SXSSFSheet hoja = libro.createSheet();
        //estilo
        XSSFFont font = (XSSFFont) libro.createFont();
        font.setBold(true);
        XSSFCellStyle style = (XSSFCellStyle) libro.createCellStyle();
        style.setFont(font);
        int nroColumna = 0;
        // Se crea una fila dentro de la hoja
        SXSSFRow row = hoja.createRow(0);
        // Se crea una celda dentro de la fila
        SXSSFCell cell = row.createCell(nroColumna);
        cell.setCellValue("EmailCliente");
        cell.setCellStyle(style);
        cell = row.createCell(++nroColumna);
        cell.setCellValue("Cantidad");
        cell.setCellStyle(style);

        int nroFila = 1;
        nroColumna = 0;
        List<Object[]> rankingArticulos = this.pedidoService.getCantidadPedidosPorCliente(sucursalId, desde, hasta);
        if (rankingArticulos != null) {
            for (Object[] object : rankingArticulos) {
                nroColumna = 0;
                row = hoja.createRow(nroFila);
                ++nroFila;
                cell = row.createCell(nroColumna);
                cell.setCellValue(((String) object[0]));
                cell = row.createCell(++nroColumna);
                cell.setCellValue(((Long) object[1]));
            }
        }
        return libro;
    }

    public List<Object[]> getIngresosData(Long sucursalId) {
        return this.pedidoService.getIngresos(sucursalId);
    }

    public SXSSFWorkbook getIngresos(Long sucursalId, Instant desde, Instant hasta) {

        // Se crea el libro
        SXSSFWorkbook libro = new SXSSFWorkbook(50); //importante !! el 50 indica el tamaño de paginación
        // Se crea una hoja dentro del libro
        SXSSFSheet hoja = libro.createSheet();
        //estilo
        XSSFFont font = (XSSFFont) libro.createFont();
        font.setBold(true);
        XSSFCellStyle style = (XSSFCellStyle) libro.createCellStyle();
        style.setFont(font);
        int nroColumna = 0;
        // Se crea una fila dentro de la hoja
        SXSSFRow row = hoja.createRow(0);
        // Se crea una celda dentro de la fila
        SXSSFCell cell = row.createCell(nroColumna);
        cell.setCellValue("Fecha");
        cell.setCellStyle(style);
        cell = row.createCell(++nroColumna);
        cell.setCellValue("Cantidad");
        cell.setCellStyle(style);

        int nroFila = 1;
        nroColumna = 0;
        List<Object[]> rankingArticulos = this.pedidoService.getIngresos(sucursalId, desde, hasta);
        if (rankingArticulos != null) {
            for (Object[] object : rankingArticulos) {
                nroColumna = 0;
                row = hoja.createRow(nroFila);
                ++nroFila;
                cell = row.createCell(nroColumna);
                cell.setCellValue(((String) object[0]));
                cell = row.createCell(++nroColumna);
                cell.setCellValue(((Double) object[1]));
            }
        }
        return libro;
    }

    public List<Object[]> getGananciasData(Long sucursalId) {
        return this.pedidoService.getGanancias(sucursalId);
    }

    public SXSSFWorkbook getGanancias(Long sucursalId, Instant desde, Instant hasta) {

        // Se crea el libro
        SXSSFWorkbook libro = new SXSSFWorkbook(50); //importante !! el 50 indica el tamaño de paginación
        // Se crea una hoja dentro del libro
        SXSSFSheet hoja = libro.createSheet();
        //estilo
        XSSFFont font = (XSSFFont) libro.createFont();
        font.setBold(true);
        XSSFCellStyle style = (XSSFCellStyle) libro.createCellStyle();
        style.setFont(font);
        int nroColumna = 0;
        // Se crea una fila dentro de la hoja
        SXSSFRow row = hoja.createRow(0);
        // Se crea una celda dentro de la fila
        SXSSFCell cell = row.createCell(nroColumna);
        cell.setCellValue("Fecha");
        cell.setCellStyle(style);
        cell = row.createCell(++nroColumna);
        cell.setCellValue("Ganancia");
        cell.setCellStyle(style);

        int nroFila = 1;
        nroColumna = 0;
        List<Object[]> rankingArticulos = this.pedidoService.getGanancias(sucursalId, desde, hasta);
        if (rankingArticulos != null) {
            for (Object[] object : rankingArticulos) {
                nroColumna = 0;
                row = hoja.createRow(nroFila);
                ++nroFila;
                cell = row.createCell(nroColumna);
                cell.setCellValue(((String) object[0]));
                cell = row.createCell(++nroColumna);
                cell.setCellValue(((Double) object[1]));
            }
        }
        return libro;
    }

    @Override
    public Pedido cambiarEstado(Long pedidoId, Estado nuevoEstado) {
        return pedidoService.cambiarEstado(pedidoId, nuevoEstado);
    }

    @Override
    public List<Pedido> getPedidosFiltrados(String rol) {
        return pedidoService.getPedidosFiltrados(rol);
    }

    public static void addMetaData(Document document) {
        document.addTitle("FACTURA DEL PEDIDO");
        document.addSubject("PEDIDO");
        document.addKeywords("PEDIDO, PDF, Detalle");
        document.addAuthor("BUEN SABOR");
        document.addCreator("BUEN SABOR");
    }

    protected static Font titulo = new Font(Font.HELVETICA, 18, Font.BOLD);
    protected static Font subtitulo = new Font(Font.HELVETICA, 14, Font.BOLD);
    protected static Font texto = new Font(Font.HELVETICA, 12, Font.NORMAL);
    protected static Font textoBold = new Font(Font.HELVETICA, 12, Font.BOLD);

    public static void addEmptyLine(Document document, int number) {
        try {
            Paragraph espacio = new Paragraph();
            for (int i = 0; i < number; i++) {
                espacio.add(new Paragraph(" "));
            }
            document.add(espacio);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addEmptyLineToTable(PdfPTable table) {
        PdfPCell emptyCell = new PdfPCell(new Phrase(" "));
        emptyCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(emptyCell);
    }

    private void addCellToTable(PdfPTable table, String header, String value) {
        PdfPCell celdaIzq = new PdfPCell(new Phrase(header, textoBold));
        celdaIzq.setBorder(Rectangle.NO_BORDER);

        PdfPCell celdaDer = new PdfPCell(new Phrase(value, texto));
        celdaDer.setBorder(Rectangle.NO_BORDER);

        // Agrega líneas en blanco solo en la columna derecha
        PdfPCell emptyCell = new PdfPCell(new Phrase(" "));
        emptyCell.setBorder(Rectangle.NO_BORDER);
        if (table.getNumberOfColumns() == 2) {  // Verifica que la tabla tenga 2 columnas (columna derecha)
            emptyCell.setColspan(2);  // Asegúrate de que la celda ocupe las dos columnas
        }

        table.addCell(celdaIzq);
        table.addCell(celdaDer);
        table.addCell(emptyCell);
    }

    @Override
    public ByteArrayOutputStream generatePedidoPDF(Long pedidoId) {
        Pedido pedido = pedidoService.getPedidoById(pedidoId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 30, 30, 30, 30);
        addMetaData(document);

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Encabezado
            PdfPTable tableCabecera = new PdfPTable(1);
            tableCabecera.setWidthPercentage(100f);

            Paragraph header = new Paragraph("FACTURA DEL PEDIDO", titulo);
            header.setAlignment(Paragraph.ALIGN_RIGHT);
            PdfPCell celda = new PdfPCell(header);
            celda.setBorder(Rectangle.NO_BORDER);
            celda.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            tableCabecera.addCell(celda);

            Image imgCabeceraRight = Image.getInstance("https://upload.wikimedia.org/wikipedia/commons/6/67/UTN_logo.jpg");
            imgCabeceraRight.scalePercent(10f);
            imgCabeceraRight.setBorder(Rectangle.NO_BORDER);
            PdfPCell logoUTN = new PdfPCell(imgCabeceraRight);
            logoUTN.setBorder(Rectangle.NO_BORDER);
            logoUTN.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            tableCabecera.addCell(logoUTN);

            document.add(tableCabecera);
            addEmptyLine(document, 1);

            // Crear tabla principal con 2 columnas
            PdfPTable mainTable = new PdfPTable(2);
            mainTable.setWidthPercentage(100);
            mainTable.setWidths(new float[]{3f, 2f});

            // Columna izquierda: detalles del pedido
            PdfPTable leftColumn = new PdfPTable(1);
            leftColumn.setWidthPercentage(100);

            // Información del pedido
            addCellToTable(leftColumn, "Pedido N°: ", String.valueOf(pedido.getId()));
            addCellToTable(leftColumn, "Fecha del Pedido: ", pedido.getFechaPedido().format(DateTimeFormatter.ISO_LOCAL_DATE));
            addCellToTable(leftColumn, "Total: ", "$" + pedido.getTotal());
            addCellToTable(leftColumn, "Tipo de Envío: ", pedido.getTipoEnvio().name());
            addCellToTable(leftColumn, "Forma de Pago: ", pedido.getFormaPago().name());

            // Información de la factura
            if (pedido.getFactura() != null) {
                addCellToTable(leftColumn, "Total Venta: ", "$" + pedido.getFactura().getTotalVenta());
            }

            PdfPCell leftColumnCell = new PdfPCell(leftColumn);
            leftColumnCell.setBorder(Rectangle.NO_BORDER);
            mainTable.addCell(leftColumnCell);

            // Columna derecha: información del cliente
            PdfPTable rightColumn = new PdfPTable(1);
            rightColumn.setWidthPercentage(100);

            // Información del cliente
            addCellToTable(rightColumn, "Cliente: ", pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido());
            addCellToTable(rightColumn, "Teléfono: ", pedido.getCliente().getTelefono());
            addCellToTable(rightColumn, "Email: ", pedido.getCliente().getEmail());

            PdfPCell rightColumnCell = new PdfPCell(rightColumn);
            rightColumnCell.setBorder(Rectangle.NO_BORDER);
            mainTable.addCell(rightColumnCell);

            document.add(mainTable);

            // Detalle del pedido
            PdfPTable detalleTable = new PdfPTable(3);
            detalleTable.setWidthPercentage(100);
            detalleTable.addCell(new PdfPCell(new Phrase("Artículo", textoBold)));
            detalleTable.addCell(new PdfPCell(new Phrase("Cantidad", textoBold)));
            detalleTable.addCell(new PdfPCell(new Phrase("Subtotal", textoBold)));

            pedido.getDetallePedidos().forEach(detalle -> {
                detalleTable.addCell(new PdfPCell(new Phrase(detalle.getArticulo().getDenominacion(), texto)));
                detalleTable.addCell(new PdfPCell(new Phrase(String.valueOf(detalle.getCantidad()), texto)));
                detalleTable.addCell(new PdfPCell(new Phrase("$" + detalle.getSubTotal(), texto)));
            });

            document.add(detalleTable);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return baos;
    }
}