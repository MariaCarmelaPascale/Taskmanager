package it.uniroma3.siw.taskmanager.authentication;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.uniroma3.siw.taskmanager.model.Credentials;


@Configuration
@EnableWebSecurity
public class AuthConfiguration extends WebSecurityConfigurerAdapter {  //la classe astratta Ã¨ un oggetto di Spring Security

	
	@Autowired
	DataSource datasource;  //punto di accesso al DB -> lo usiamo poichÃ¨ SpringSecurity lavora a livello piÃ¹ basso

	public void configure(HttpSecurity http) throws Exception {  // quali sono le policies effettive di autenticazione e autorizzazione -> chi accede a cosa
		// catena di chiamate
		http
				
				/*Paragrafo dove specifichiamo CHI puÃ² accedere a COSA*/
				// CHI può accedere a QUALI pagine
				.authorizeRequests()
				// CHI può mandare richieste GET
				.antMatchers(HttpMethod.GET, "/", "/index", "/login", "/users/register").permitAll()
				// CHI può mandare richeste POST
				.antMatchers(HttpMethod.POST, "/login", "/users/register").permitAll()
				// qualsiasi url che comincia con /admin/ può essere acceduto da chi ha il ruolo di admin
				.antMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(Credentials.ADMIN_ROLE)
				.antMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(Credentials.ADMIN_ROLE)
				// PAGINE RIMASTE A TUTTI
				.anyRequest().authenticated()  // le altre accessibili con qualsiasi ruolo
				.and()
				/*Paragrafo dove specifichiamo dove avviene il login*/
				.formLogin().defaultSuccessUrl("/home")  //non dobbiamo definire /login poichÃ¨ la vista viene giÃ  "preconfezionata" da SpringSecurity
				.and()
				/*Paragrafo dove specifichiamo come avviene il logout*/
				.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/index")  //non è necessario implementarli poichè vengono già  gestiti dal framework
				//interrompe la sessione quando viene fatto logout
				.invalidateHttpSession(true)
				.clearAuthentication(true).permitAll();
	}
	
	//Dove l'applicazione può trovare le credenziali
	public void configure(AuthenticationManagerBuilder auth) throws Exception {  //dove SpringSecurity deve andare a prendere le credenziali nel DB per confrontarle con quelle date dall'utente
		
		auth.jdbcAuthentication()
				//dove trovare i dati
				.dataSource(this.datasource)
				//come ottenere lo username e il ruolo associato ad esso
				.authoritiesByUsernameQuery("SELECT user_name,role FROM credentials WHERE user_name=?")
				//come ottenere username, password e un flag che indica se lo user Ã¨ abilitato o no (sempre abilitati)
				.usersByUsernameQuery("SELECT user_name, password, 1 as enabled FROM credentials WHERE user_name=?");
	}
	
	@Bean // l'oggetto restituito dal metodo viene salvato -> l'applicazione non dovrÃ  ricercarlo e possiamo quindi ottenerlo tramite auowired -> sinonimo di @Component
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
