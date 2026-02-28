package com.unieus.garajea.desktop.ui.erreserbak;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.unieus.garajea.core.presentation.agenda.AgendaBlokeaDTO;
import com.unieus.garajea.core.services.ErreserbaService;
import com.unieus.garajea.core.services.context.ServiceContext;
import com.unieus.garajea.desktop.bootstrap.DesktopAppBootstrap;
import com.unieus.garajea.model.dto.ErreserbaInfoDTO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Component;

public class ErreserbakPanel extends JPanel {
	
	private LocalDate eguna;
	private JLabel egunaLabel;
	private JPanel kabinaAgendakPanel;
	// agendak garbitu eta birpopulatzeko:
	private Map<String, JPanel> kabinaPanelenMapa;

    public ErreserbakPanel() {
        initUI();
		kargatuKabinenDatuak();
    }
	
    private void initUI() {

        setLayout(new BorderLayout(10, 10));
		
		// hasierako egoera: gaurko data erabili
		eguna = LocalDate.now();
		
		// GOIKO PANELA (data)
		JPanel goikoKontrolPanel = new JPanel(
			new FlowLayout(FlowLayout.LEFT, 10, 5)
		);	

		goikoKontrolPanel.setBorder(
			BorderFactory.createEmptyBorder(5, 10, 5, 10)
		);		
			
		egunaLabel = new JLabel(formateatuEguna(eguna));
		egunaLabel.setFont(
			egunaLabel.getFont().deriveFont(Font.BOLD)
		);	
		
		// ---- JDatePicker ----
		DatePickerSettings settings = new DatePickerSettings();

		Locale euskaraLocale = Locale.of("eu", "ES");

		settings.setLocale(euskaraLocale);
		settings.setFirstDayOfWeek(DayOfWeek.MONDAY);
		settings.setFormatForDatesCommonEra("yyyy-MM-dd");
		settings.setFormatForDatesBeforeCommonEra("uuuu-MM-dd");
		settings.setTranslationToday("Gaur");
		settings.setTranslationClear("Garbitu");

		DatePicker datePicker = new DatePicker(settings);

		datePicker.setDate(eguna);
		
		// Listener: egoera eguneratu eta birkargatu agendak
		datePicker.addDateChangeListener(event -> {
			LocalDate newDate = event.getNewDate();
			if (newDate != null) {
				eguna = newDate;
				egunaLabel.setText(formateatuEguna(eguna));

				kargatuKabinenDatuak();
			}
		});
		
		goikoKontrolPanel.add(new JLabel("Eguna:"));
		goikoKontrolPanel.add(egunaLabel);
		goikoKontrolPanel.add(datePicker);

		add(goikoKontrolPanel, BorderLayout.NORTH);
		
		// ERDIKO PANELA (agendak)
		kabinaAgendakPanel =
			new JPanel(new GridLayout(1, 5, 10, 0));

		kabinaAgendakPanel.setBorder(
			BorderFactory.createEmptyBorder(10, 10, 10, 10)
		);

		kabinaPanelenMapa = new LinkedHashMap<>();
		
		List<String> kabinaIzenak = List.of(
			"Kabina01",
			"Kabina02",
			"Kabina03",
			"Kabina04",
			"Kabina05"
		);
		
		for (String kabinaIzena : kabinaIzenak) {

			JPanel agendaCol = new JPanel(new BorderLayout(5, 5));

			JLabel kabinaLabel = new JLabel(
				kabinaIzena,
				SwingConstants.CENTER
			);
			kabinaLabel.setFont(
				kabinaLabel.getFont().deriveFont(Font.BOLD)
			);

			JPanel timelineBody = new JPanel();
			timelineBody.setLayout(
				new BoxLayout(timelineBody, BoxLayout.Y_AXIS)
			);

			JScrollPane scrollPane = new JScrollPane(timelineBody);
			scrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
			);
			scrollPane.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
			);

			agendaCol.add(kabinaLabel, BorderLayout.NORTH);
			agendaCol.add(scrollPane, BorderLayout.CENTER);

			kabinaAgendakPanel.add(agendaCol);
			kabinaPanelenMapa.put(kabinaIzena, timelineBody);
		}

		add(kabinaAgendakPanel, BorderLayout.CENTER);
				
	}
	
	private void garbituKabinenAgendak() {
		for (JPanel timelineBody : kabinaPanelenMapa.values()) {
			timelineBody.removeAll();
		}
	}

	private void kargatuKabinenDatuak() {

		garbituKabinenAgendak();

		// Eguneko erreserbak lortu

        try (ServiceContext ZerbitzuEsparrua =
                 DesktopAppBootstrap.getServiceContextFactory().open()) {

            ErreserbaService erreserbaZerbitzua = ZerbitzuEsparrua.getErreserbaService();

			List<ErreserbaInfoDTO> egunekoErreserbak =
				erreserbaZerbitzua.bilatuDatakoErreserbak(eguna);

			// kabina bakoitzeko agenda bat sortu
			Map<String, List<AgendaBlokeaDTO>> kabinaAgendakMapa =
				ZerbitzuEsparrua.getErreserbaAgendaBuilder()
					.sortuAgendakKabinaka(
						egunekoErreserbak,
						eguna,
						eguna
					);

			// renderizatu kabina bakoitzeko agenda bere timelineBody JPanel-ean
			erakutsiKabinenAgendak(kabinaAgendakMapa);

			// Swing eguneratu
			revalidate();
			repaint();

		} catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Errore tekniko bat gertatu da agendak kargatzean.",
                "Errorea",
                JOptionPane.ERROR_MESSAGE
            );
        }
	}

	private void erakutsiKabinenAgendak(Map<String, List<AgendaBlokeaDTO>> kabinaAgendakMapa){

		for (Map.Entry<String, List<AgendaBlokeaDTO>> entry
        : kabinaAgendakMapa.entrySet()) {
			JPanel timelineBody = kabinaPanelenMapa.get(entry.getKey());
			List<AgendaBlokeaDTO> agenda = entry.getValue();

			for (AgendaBlokeaDTO blokea : agenda) {
				if (blokea.getMota() == AgendaBlokeaDTO.Mota.LIBREA) {
    				continue;
				}
				JLabel lerroa = sortuAgendaLerroa(blokea);
				timelineBody.add(lerroa);
			}
		}
	}

	private JLabel sortuAgendaLerroa(AgendaBlokeaDTO blokea) {

		switch (blokea.getMota()) {

			case EGUNBANATZAILEA -> {
				JLabel label = new JLabel(
					"= " + blokea.getEgunarenEtiketa() + " ="
				);
				label.setAlignmentX(Component.LEFT_ALIGNMENT);
				label.setOpaque(true); // GARRANTZIZKOA: JLabel-aren atzealdeko kolorea ikusteko beharrezkoa da
				label.setBackground(new Color(60, 63, 65)); // gris 'pizarra'
				label.setForeground(new Color(220, 220, 220)); // txuri suabea
				label.setFont(label.getFont().deriveFont(Font.BOLD));
				label.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

				return label;
			}

			case ERRESERBA -> {
				String langileIzena = " - ";
				if (blokea.getErreserbaInfo().getLangileIzena() != null) {
					langileIzena = blokea.getErreserbaInfo().getLangileIzena();
				} 
				String html = """
					<html>
						<b>%s - %s</b> | %s<br>
						| %s<br>
						<i>%s</i>
					</html>
					""".formatted(
						blokea.getHasieraOrdua(),
						blokea.getAmaieraOrdua(),
						langileIzena,
						blokea.getErreserbaInfo().getBezeroIzenAbizenak(),
						blokea.getErreserbaInfo().getIbilgailuInfo()
					);

				JLabel label = new JLabel(html);
				label.setAlignmentX(Component.LEFT_ALIGNMENT);
				// padding eman labelari, eta beheko borderra gehitu, labelen arteko bereizketa hobetzeko
				label.setBorder(
					BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(4, 6, 4, 6), 
						BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY)
				));

				return label;
			}

			default -> throw new IllegalStateException(
				"AgendaBlokeaDTO mota ezezaguna: " + blokea.getMota()
			);
		}
	}

	private String formateatuEguna(LocalDate eguna) {
		DateTimeFormatter formatter =
			DateTimeFormatter.ofPattern("yyyy-MM-dd (EEEE)", Locale.of("eu"));
		return eguna.format(formatter);
	}
}
