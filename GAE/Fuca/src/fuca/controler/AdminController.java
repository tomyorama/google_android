package fuca.controler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.appengine.api.blobstore.BlobKey;

import fuca.model.PMF;
import fuca.model.Termin;
import fuca.model.User;

@Controller
@RequestMapping("/Admin")
public class AdminController {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
		dateFormat.setLenient(false);

		// true passed to CustomDateEditor constructor means convert empty
		// String to null
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}

	@RequestMapping(value = "/AddTermin", method = RequestMethod.GET)
	public String terimn(ModelMap model) {
		model.addAttribute("termin", new Termin());
		return "addtermin";
	}

	@RequestMapping(value = "/AddTermin", method = RequestMethod.POST)
	public String terimn(@Valid Termin termin, BindingResult result) {

		if (result.hasErrors()) {
			return "addtermin";
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(termin);
		} finally {
			pm.close();
		}
		return "redirect:/Termin";
	}
	
//	@RequestMapping(value = "/EditTermin", method = RequestMethod.GET)
//	public String EditTermin(@PathVariable long id,ModelMap model) {
//		//
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		javax.jdo.Query q = pm.newQuery(Termin.class);
//		q.setFilter("id == teminKey");
//		q.setOrdering("date desc");
//		q.declareParameters("String teminKey");
//		q.setRange(0, 1);
//		try {
//			@SuppressWarnings("unchecked")
//			List<Termin> termins = (List<Termin>) q.execute(id);
//
//			if (termins != null && termins.size() > 0) {
//				Termin tmpTermin = termins.get(0);
//				model.addAttribute("termin", tmpTermin);
//
//			} else {
//				model.addAttribute("termin", new Termin());
//			}
//		} finally {
//			q.closeAll();
//		}
//		//
//		return "addtermin";
//	}
//
//	@RequestMapping(value = "/EditTermin", method = RequestMethod.POST)
//	public String EditTermin(@Valid Termin termin, BindingResult result) {
//
//		if (result.hasErrors()) {
//			return "addtermin";
//		}
//
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			pm.makePersistent(termin);
//		} finally {
//			pm.close();
//		}
//		return "redirect:/Termin";
//	}
	
	@RequestMapping(value = "/AddUser", method = RequestMethod.GET)
	public String user(ModelMap model) {
		model.addAttribute("user", new User());
		return "adduser";
	}

	@RequestMapping(value = "/AddUser", method = RequestMethod.POST)
	public String addUser(@Valid User user, BindingResult result) {

		if (result.hasErrors()) {
			return "adduser";
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(user);
		} finally {
			pm.close();
		}
		return "redirect:/Users";
	}
}
