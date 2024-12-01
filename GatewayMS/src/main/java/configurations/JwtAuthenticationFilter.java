//package configurations;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//import utility.JwtUtil;
//
//@Component
//public class JwtAuthenticationFilter implements WebFilter {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        if (request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//            String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//                String token = authorizationHeader.substring(7);
//                if (jwtUtil.validateToken(token)) {
//                    return chain.filter(exchange);
//                }
//            }
//        }
//        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//        return exchange.getResponse().setComplete();
//    }
//}
//
