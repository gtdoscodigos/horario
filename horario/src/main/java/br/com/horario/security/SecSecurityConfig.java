package br.com.horario.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecSecurityConfig {
	
	@Autowired
	private UserDetailServiceImpl userDetailServiceImpl;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		http.authorizeHttpRequests(
				auth -> auth
				//Qualquer tipo de permissaão consegue acessar esse @Controller
				.requestMatchers("/signin", "/signup").permitAll()
				//Quem possuir algum dos dois perfis pode acessar o @controller
				.requestMatchers("/preferencia").hasAnyAuthority("professor", "coordenador_curso")
				.requestMatchers("/disponibilidade").hasAnyAuthority("professor","coordenador_curso")
				.requestMatchers("/docente").hasAuthority("administrador")
				//Qualquer requisição ao @controller o usuário precisa estar autenticado
				.anyRequest().authenticated()
				)
				.formLogin(formLogin -> formLogin
						//Direciona para esse @controller quando o login está correto
						.defaultSuccessUrl("/principal", true)
						.permitAll()
						)
				.rememberMe(rememberMe -> rememberMe.key("AbcdEfghIjkl..."))
				.logout(logout -> logout.logoutUrl("/signout").permitAll());
		
		return http.build();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
		throws Exception {
		//Serve de exemplo para gerar uma senha criptografada
		BCryptPasswordEncoder b = new BCryptPasswordEncoder();
		System.out.println(b.encode("123456"));
		//Cripitografia a senha para salvar no banco de dados
		auth.userDetailsService(userDetailServiceImpl).passwordEncoder(new BCryptPasswordEncoder());
	}

}
