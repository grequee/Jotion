package com.jotion.lp.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jotion.lp.DTO.CriarNotaDTO;
import com.jotion.lp.Service.NotaService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint(value = "/ws/notas/{notaId}")
public class NotaWebSocketEndpoint {

    private static final Map<Long, Set<Session>> activeSessions = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static JwtUtil jwtUtil;
    private static NotaService notaService;

    @Autowired
    public void setJwtUtil(JwtUtil jwtUtil) {
        NotaWebSocketEndpoint.jwtUtil = jwtUtil;
    }

    @Autowired
    public void setNotaService(NotaService notaService) {
        NotaWebSocketEndpoint.notaService = notaService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("notaId") Long notaId) throws IOException {
        String query = session.getQueryString();
        String token = null;

        if (query != null && query.contains("token=")) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("token=")) {
                    token = param.substring(6);
                    break;
                }
            }
        }

        if (token == null) {
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Token ausente"));
            return;
        }

        try {
            String nomeUsuario = jwtUtil.validarToken(token);
            session.getUserProperties().put("nomeUsuario", nomeUsuario);

            // Verifica se tem permissão (lança exceção se não tiver)
            notaService.buscarEntidadePorId(notaId, nomeUsuario);

            activeSessions.computeIfAbsent(notaId, k -> new CopyOnWriteArraySet<>()).add(session);

        } catch (Exception e) {
            e.printStackTrace();
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Token invalido ou sem permissao"));
        }
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("notaId") Long notaId) {
        String nomeUsuario = (String) session.getUserProperties().get("nomeUsuario");

        try {
            CriarNotaDTO dto = objectMapper.readValue(message, CriarNotaDTO.class);
            notaService.atualizar(notaId, dto, nomeUsuario);

            String broadcastMsg = objectMapper.writeValueAsString(Map.of(
                "titulo", dto.getTitulo() != null ? dto.getTitulo() : "",
                "conteudo", dto.getConteudo() != null ? dto.getConteudo() : "",
                "editadoPor", nomeUsuario
            ));

            Set<Session> sessions = activeSessions.get(notaId);
            if (sessions != null) {
                for (Session s : sessions) {
                    if (s.isOpen() && !s.getId().equals(session.getId())) {
                        try {
                            s.getBasicRemote().sendText(broadcastMsg);
                        } catch (IOException e) {
                            // Ignora erro ao enviar
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro no WebSocket: " + e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("notaId") Long notaId) {
        Set<Session> sessions = activeSessions.get(notaId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                activeSessions.remove(notaId);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Erro no WebSocket: " + throwable.getMessage());
    }
}
