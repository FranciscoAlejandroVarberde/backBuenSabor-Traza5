package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.domain.dto.pedido.PedidoFullDto;
import com.entidades.buenSabor.domain.entities.Pedido;
import com.entidades.buenSabor.domain.entities.PreferenceMP;
import com.entidades.buenSabor.repositories.PreferenceMPRepository;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.resources.preference.PreferenceItem;
import com.mercadopago.resources.preference.PreferencePaymentMethod;
import com.mercadopago.resources.preference.PreferencePaymentMethods;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MercadoPagoService {
    private PreferenceMPRepository preferenceMPRepository;

    public MercadoPagoService(PreferenceMPRepository preferenceRepository) {
        this.preferenceMPRepository = preferenceRepository;
    }
    public PreferenceMP createPreference(PedidoFullDto pedido) {
        try {
            MercadoPagoConfig.setAccessToken("TEST-5520850756347883-052115-87619eea0fe6198035d24d9a90cee463-1070043938");

            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(String.valueOf(pedido.getId()))
                    .title("Pedido Buen Sabor")
                    .description("Pedido realizado desde el carrito de compras")
                    .pictureUrl("https://img-global.cpcdn.com/recipes/0709fbb52d87d2d7/1200x630cq70/photo.jpg")
                    .quantity(1)
                    .currencyId("ARS") // Cambia "ARG" por "ARS"
                    .unitPrice(new BigDecimal(pedido.getTotal()))
                    .build();
            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(itemRequest);

//            String successUrl = "http://localhost:5174/carrito/1?i=" + pedido.getId();

            PreferenceBackUrlsRequest backURL = PreferenceBackUrlsRequest.builder()
                    .success("https://www.youtube.com/")
                    .pending("https://www.youtube.com/")
                    .failure("https://www.youtube.com/")
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backURL)
                    .autoReturn("approved")
                    .build();
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            PreferenceMP mpPreference = new PreferenceMP();
            mpPreference.setStatusCode(preference.getResponse().getStatusCode());
            mpPreference.setId(preference.getId());
            mpPreference.setIdPedido(pedido.getId());

            // Información adicional
            // Asignar fechas
            mpPreference.setFechaCreacion(preference.getDateCreated());

            mpPreference.setTotal(preference.getItems().stream()
                    .map(PreferenceItem::getUnitPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));

            preferenceMPRepository.save(mpPreference);
            return mpPreference;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
