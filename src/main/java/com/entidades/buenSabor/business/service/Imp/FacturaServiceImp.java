package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.Base.BaseServiceImp;
import com.entidades.buenSabor.business.service.FacturaService;
import com.entidades.buenSabor.business.service.PreferenceMPService;
import com.entidades.buenSabor.domain.entities.Factura;
import com.entidades.buenSabor.domain.entities.Pedido;
import com.entidades.buenSabor.domain.entities.PreferenceMP;
import com.entidades.buenSabor.repositories.FacturaRepository;
import com.entidades.buenSabor.repositories.PedidoRepository;
import com.entidades.buenSabor.repositories.PreferenceMPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class FacturaServiceImp extends BaseServiceImp<Factura, Long> implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PreferenceMPService preferenceMPService;

    @Override
    public Factura crearFactura(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        PreferenceMP preferenceMP = null;
        try {
            preferenceMP = preferenceMPService.obtenerPorIdPedido(pedidoId);
        } catch (Exception e) {
            // Log the exception if necessary
            System.out.println("PreferenceMP no encontrado para el pedidoId: " + pedidoId);
        }

        Factura factura = Factura.builder()
                .fechaFcturacion(LocalDate.now())
                .formaPago(pedido.getFormaPago())
                .totalVenta(pedido.getTotal())
                .mpPreferenceId(preferenceMP != null ? preferenceMP.getId() : "0") // Asigna 0 si preferenceMP es null
                .build();

        facturaRepository.save(factura);

        pedido.setFactura(factura);
        pedidoRepository.save(pedido);

        return factura;
    }
}
