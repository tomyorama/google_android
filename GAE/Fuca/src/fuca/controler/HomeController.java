package fuca.controler;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import fuca.model.Comment;
import fuca.model.PMF;
import fuca.model.Termin;

@Controller
@RequestMapping("/")
public class HomeController {

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ModelAndView mav = new ModelAndView("termindetails");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		javax.jdo.Query q = pm.newQuery(Termin.class);
		q.setOrdering("date desc");
		q.setRange(0, 1);
		try {

			@SuppressWarnings("unchecked")
			List<Termin> termins = (List<Termin>) q.execute();
			if (termins != null && termins.size() > 0) {
				ModelAndView editTermin = new TerminController().Details(
						termins.get(0).getId(), request, response);
				editTermin.setViewName("termindetailsX");
				editTermin.addObject("readonly", false);
				return editTermin;
			} else {
				Termin dummy = new Termin();
				dummy.setName("Dummy");
				mav.addObject("termin", dummy);
			}
		} finally {
			q.closeAll();
		}
		Comment comment = new Comment();
		if (request.getUserPrincipal() != null) {
			comment.setUser(request.getUserPrincipal().getName());
		}
		mav.addObject("comment", comment);
		return mav;
	}


	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String getAddCustomerPage(ModelMap model) {

		return "add";

	}

}
