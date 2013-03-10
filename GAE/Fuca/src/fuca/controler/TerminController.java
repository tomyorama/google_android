package fuca.controler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import fuca.model.Comment;
import fuca.model.PMF;
import fuca.model.Termin;
import fuca.model.Termin.Player;
import fuca.model.User;
import fuca.viewmodel.TerminGridResult;
import fuca.viewmodel.TerminSearch;

@Controller
@RequestMapping("/Termin")
public class TerminController {

	@RequestMapping(method = RequestMethod.GET)
	public String Termin(ModelMap model) {
		return "termin";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/termindata")
	public @ResponseBody
	TerminGridResult getTerminData(TerminSearch search, Model model)
			throws Exception {
		int rowNum = search.getPage() != 0 ? search.getPage() : 1;
		int rowLimit = search.getRows() != 0 ? search.getRows() : 10;

		TerminGridResult result = new TerminGridResult();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		javax.jdo.Query q = pm.newQuery(Termin.class);
		q.setOrdering("date desc");
		// q.setRange ((rowNum - 1) * rowLimit, rowLimit + rowNum * rowLimit);
		try {
			@SuppressWarnings("unchecked")
			List<Termin> termins = (List<Termin>) q.execute();

			result.setPage(rowNum);
			int max = termins.size();
			result.setRecords(max);
			result.setTotal((int) Math.ceil((double) termins.size() / rowLimit));
			int maxLimit = rowLimit > max ? max
					: (rowNum * rowLimit) > max ? max : (rowNum * rowLimit);
			result.setRows(termins.subList((rowNum - 1) * rowLimit, maxLimit));
		} finally {
			q.closeAll();
		}

		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/Details/{id}")
	public ModelAndView Details(@PathVariable long id,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ModelAndView mav = new ModelAndView("termindetails");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		javax.jdo.Query q = pm.newQuery(Termin.class);
		q.setFilter("id == teminKey");
		q.setOrdering("date desc");
		q.declareParameters("String teminKey");
		q.setRange(0, 1);
		try {
			@SuppressWarnings("unchecked")
			List<Termin> termins = (List<Termin>) q.execute(id);

			if (termins != null && termins.size() > 0) {
				Termin tmpTermin = termins.get(0);
				mav.addObject("termin", tmpTermin);
				DateFormat f = new SimpleDateFormat("dd.MM.yyyy");
				mav.addObject("tmpdate",
						f.format(tmpTermin.getDate() != null ? tmpTermin
								.getDate() : new Date()));

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
		mav.addObject("readonly", true);
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/addComment/{id}")
	public ModelAndView addComment(@PathVariable long id,
			@Valid Comment comment, BindingResult result,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		ModelAndView mav = new ModelAndView("comment");
		comment.setDateCreated(Calendar.getInstance().getTime());
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Termin termin = pm.getObjectById(Termin.class, id);
			mav.addObject("termin", termin);
			if (termin.getComments() != null) {
				termin.getComments().add(comment);
			} else {
				List<Comment> comments = new ArrayList<Comment>();
				comments.add(comment);
				termin.setComments(comments);
			}
			pm.makePersistent(termin);
		} finally {
			pm.close();
		}
		mav.addObject("item", comment);
		return mav;
	}
	

	@RequestMapping(method = RequestMethod.POST, value = "/AddUsers1/{id}")
	public @ResponseBody
	Boolean addUser1(@PathVariable long id,
			@RequestParam("usersIds[]") Collection<? extends Long> usersIds,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		javax.jdo.Query q = pm.newQuery(User.class);
		try {
			List<User> allUsers = (List<User>) q.execute();
			Termin termin = pm.getObjectById(Termin.class, id);
			if (termin.getTeam1Players() == null) {
				Set<Player> players = new HashSet<Player>();
				termin.setTeam1Players(players);
				for (Long playerId : usersIds) {
					players.add(playerFromUsers(playerId, allUsers));
				}

			} else {
				Set<Player> players = new HashSet<Player>();
				for (Long playerId : usersIds) {
					players.add(playerFromUsers(playerId, allUsers));
				}
				termin.getTeam1Players().addAll(players);
			}
			pm.makePersistent(termin);
		} finally {
			pm.close();
		}
		return true;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/AddUsers2/{id}")
	public @ResponseBody
	Boolean addUser2(@PathVariable long id,
			@RequestParam("usersIds[]") Collection<? extends Long> usersIds,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		javax.jdo.Query q = pm.newQuery(User.class);
		try {
			List<User> allUsers = (List<User>) q.execute();
			Termin termin = pm.getObjectById(Termin.class, id);
			if (termin.getTeam2Players() == null) {
				Set<Player> players = new HashSet<Player>();
				for (Long playerId : usersIds) {
					players.add(playerFromUsers(playerId, allUsers));
				}
				termin.setTeam2Players(players);
			} else {
				Set<Player> players = new HashSet<Player>();
				for (Long playerId : usersIds) {
					players.add(playerFromUsers(playerId, allUsers));
				}
				termin.getTeam2Players().addAll(players);
			}
			pm.makePersistent(termin);
		} finally {
			pm.close();
		}
		return true;
	}

	private Player playerFromUsers(Long userId, List<User> allUsers) {
		Player pl = new Player(userId);
		for (User user : allUsers) {
			if (user.getId().equals(userId)) {
				pl.setName(user.getName());
				pl.setNickname(user.getNickname());
				break;
			}
		}
		return pl;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/RemoveUsers1/{id}/{userId}")
	public String removeUser1(@PathVariable long id, @PathVariable long userId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {

			Termin termin = pm.getObjectById(Termin.class, id);
			termin.getTeam1Players().remove(new Player(userId));
			pm.makePersistent(termin);
		} finally {
			pm.close();
		}
		return "redirect:/";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/RemoveUsers2/{id}/{userId}")
	public String removeUser2(@PathVariable long id, @PathVariable long userId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {

			Termin termin = pm.getObjectById(Termin.class, id);
			termin.getTeam2Players().remove(new Player(userId));
			pm.makePersistent(termin);
		} finally {
			pm.close();
		}
		return "redirect:/";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/Confirm1/{id}/{userId}/{comfirmed}")
	public @ResponseBody
	Map<String, String> comfirmUser1(@PathVariable long id,
			@PathVariable long userId, @PathVariable boolean comfirmed,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> retVal = new HashMap<String, String>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {

			Termin termin = pm.getObjectById(Termin.class, id);

			for (Player player : termin.getTeam1Players()) {
				if (player.getUserId() == userId) {
					player.setConfirmed(comfirmed);
					break;
				}
			}
			pm.makePersistent(termin);
		} finally {
			pm.close();
		}
		retVal.put("src",
				comfirmed ? "../../Content/images/1354390215_Check.png"
						: "../../Content/images/1354390210_button_cancel.png");
		retVal.put("alt", comfirmed ? "Dolazim" : "Nedolazim");
		retVal.put("title", comfirmed ? "Dolazim" : "Nedolazim");
		retVal.put("href", String.format("/Termin/Confirm1/%s/%s/%s", id,
				userId, !comfirmed));
		return retVal;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/Confirm2/{id}/{userId}/{comfirmed}")
	public @ResponseBody
	Map<String, String> comfirmUser2(@PathVariable long id,
			@PathVariable long userId, @PathVariable boolean comfirmed,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> retVal = new HashMap<String, String>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {

			Termin termin = pm.getObjectById(Termin.class, id);
			for (Player player : termin.getTeam2Players()) {
				if (player.getUserId() == userId) {
					player.setConfirmed(comfirmed);
					break;
				}
			}
			pm.makePersistent(termin);
		} finally {
			pm.close();
		}
		retVal.put("src",
				comfirmed ? "../../Content/images/1354390215_Check.png"
						: "../../Content/images/1354390210_button_cancel.png");
		retVal.put("alt", comfirmed ? "Dolazim" : "Nedolazim");
		retVal.put("title", comfirmed ? "Dolazim" : "Nedolazim");
		retVal.put("href", String.format("/Termin/Confirm2/%s/%s/%s", id,
				userId, !comfirmed));
		return retVal;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/RemoveComment/{id}/{commentId}")
	public @ResponseBody
	Map<String, String> RemoveComment(@PathVariable long id,
			@PathVariable long commentId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, String> retVal = new HashMap<String, String>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			Termin termin = pm.getObjectById(Termin.class, id);
			for (Comment comment : termin.getComments()) {
				if (comment.getLongId() == commentId) {
					termin.getComments().remove(comment);
					break;
				}
			}
			pm.makePersistent(termin);
		} finally {
			pm.close();
		}
		retVal.put("Success", "OK");
		return retVal;
	}
}