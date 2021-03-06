package fuca.controler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;

import fuca.model.PMF;
import fuca.model.Termin;
import fuca.model.Termin.Player;
import fuca.model.User;
import fuca.viewmodel.TerminSearch;
import fuca.viewmodel.UserGridResult;

@Controller
@RequestMapping("/Users")
public class UserController {
	private BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	@RequestMapping(method = RequestMethod.GET)
	public String User(ModelMap model) {
		return "users";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/userdata")
	public @ResponseBody
	UserGridResult getTerminData(TerminSearch search, Model model)
			throws Exception {
		int rowNum = search.getPage() != 0 ? search.getPage() : 1;
		int rowLimit = search.getRows() != 0 ? search.getRows() : 10;

		UserGridResult result = new UserGridResult();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		javax.jdo.Query q = pm.newQuery(User.class);
		// q.setRange ((rowNum - 1) * rowLimit, rowLimit + rowNum * rowLimit);
		try {
			@SuppressWarnings("unchecked")
			List<User> users = (List<User>) q.execute();
			result.setPage(rowNum);
			int max = users.size();
			result.setRecords(max);
			result.setTotal((int) Math.ceil((double) users.size() / rowLimit));
			int maxLimit = rowLimit > max ? max
					: (rowNum * rowLimit) > max ? max : (rowNum * rowLimit);
			result.setRows(users.subList((rowNum - 1) * rowLimit, maxLimit));
		} finally {
			q.closeAll();
		}

		return result;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/userdata/{id}/{team}")
	public @ResponseBody
	UserGridResult getTerminData(TerminSearch search,
			@PathVariable("id") long terminid,
			@PathVariable("team") long teamId, Model model) throws Exception {
		int rowNum = search.getPage() != 0 ? search.getPage() : 1;
		int rowLimit = search.getRows() != 0 ? search.getRows() : 10;

		UserGridResult result = new UserGridResult();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		javax.jdo.Query q = pm.newQuery(User.class);
		javax.jdo.Query qTermin = pm.newQuery(Termin.class);
		qTermin.setFilter("id == teminKey");
		qTermin.setOrdering("date desc");
		qTermin.declareParameters("String teminKey");
		try {
			List<Termin> termins = (List<Termin>) qTermin.execute(terminid);
			@SuppressWarnings("unchecked")
			List<User> users = (List<User>) q.execute();
			List<User> usersToRemove = new ArrayList<User>();
			usersToRemove.addAll(users);
			if (termins != null && termins.size() > 0) {
				Termin tmpTermin = termins.get(0);
				for (User user : users) {
					if (teamId == 1
							&& tmpTermin.getTeam1Players() != null
							&& tmpTermin.getTeam1Players().contains(
									new Player(user.getId()))) {
						usersToRemove.remove(user);
					} else if (teamId == 2
							&& tmpTermin.getTeam2Players() != null
							&& tmpTermin.getTeam2Players().contains(
									new Player(user.getId()))) {
						usersToRemove.remove(user);
					}
				}
			}
			result.setPage(rowNum);
			int max = usersToRemove.size();
			result.setRecords(max);
			result.setTotal((int) Math.ceil((double) usersToRemove.size()
					/ rowLimit));
			int maxLimit = rowLimit > max ? max
					: (rowNum * rowLimit) > max ? max : (rowNum * rowLimit);
			result.setRows(usersToRemove.subList((rowNum - 1) * rowLimit,
					maxLimit));
		} finally {
			q.closeAll();
		}

		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/Details/{id}")
	public ModelAndView Details(@PathVariable long id) throws Exception {

		ModelAndView mav = new ModelAndView("userdetails");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		javax.jdo.Query q = pm.newQuery(User.class);
		q.setFilter("id == teminKey");
		q.declareParameters("String teminKey");
		q.setRange(0, 1);
		try {
			@SuppressWarnings("unchecked")
			List<User> users = (List<User>) q.execute(id);

			if (users != null && users.size() > 0) {
				User tmpUser = users.get(0);
				mav.addObject("user", tmpUser);
				BlobKey blobString = tmpUser.getPicture();
				if (blobString != null)
					mav.addObject("blobString", blobString.getKeyString());
				else {
					mav.addObject("blobString", "dummy");
				}
			} else {
				User dummy = new User();
				dummy.setName("Dummy");
				mav.addObject("user", dummy);
			}
		} finally {
			q.closeAll();
		}

		return mav;
	}

	@RequestMapping(value = "/EditUser/{id}", method = RequestMethod.GET)
	public ModelAndView editUser(@PathVariable long id) {

		ModelAndView mav = new ModelAndView("edituser");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		javax.jdo.Query q = pm.newQuery(User.class);
		q.setFilter("id == teminKey");
		q.declareParameters("String teminKey");
		q.setRange(0, 1);
		try {
			@SuppressWarnings("unchecked")
			List<User> users = (List<User>) q.execute(id);
			User tmpUser = users.get(0);
			mav.addObject("userToSave", tmpUser);
			BlobKey blobString = tmpUser.getPicture();
			mav.addObject("blobString",
					blobString != null ? blobString.getKeyString() : null);
		} finally {
			q.closeAll();
		}
		return mav;
	}

	@RequestMapping(value = "/EditUser", method = RequestMethod.POST)
	public String editUser(@Valid User userToSave, BindingResult result,
			HttpServletRequest request) {

		if (result.hasErrors()) {
			return "edituser";
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		javax.jdo.Query q = pm.newQuery(User.class);
		q.setFilter("id == teminKey");
		q.declareParameters("String teminKey");
		q.setRange(0, 1);
		try {
			List<User> users = (List<User>) q.execute(userToSave.getId());
			User tmpUser = users.get(0);
			BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
			BlobKey blobKey = null;
			if (bs.getUploads(request) != null
					&& bs.getUploads(request).get("picture") != null
					&& bs.getUploads(request).get("picture").size() > 0) {
				blobKey = bs.getUploads(request).get("picture").get(0);
				final BlobInfo blobInfo = new BlobInfoFactory()
						.loadBlobInfo(blobKey);
				long size = blobInfo.getSize();
				if (size > 0) {
					userToSave.setPicture(blobKey);
				} else {
					bs.delete(blobKey);
					userToSave.setPicture(tmpUser.getPicture());
				}
			} else {
				userToSave.setPicture(tmpUser.getPicture());
			}
			pm.makePersistent(userToSave);
		} finally {
			pm.close();
		}
		return "redirect:/Users/Details/" + userToSave.getId();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	public String Upload(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		long id = Long.parseLong(request.getParameter("id"));
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
		List<BlobKey> blobKey = blobs.get("myFile");

		PersistenceManager pm = PMF.get().getPersistenceManager();
		javax.jdo.Query q = pm.newQuery(User.class);
		q.setFilter("id == teminKey");
		q.declareParameters("String teminKey");
		q.setRange(0, 1);
		try {
			@SuppressWarnings("unchecked")
			List<User> users = (List<User>) q.execute(id);

			if (users != null && users.size() > 0) {
				User tmpUser = users.get(0);
				tmpUser.setPicture(blobKey.get(0));
				pm.makePersistent(tmpUser);
			} else {
			}

		} finally {
			q.closeAll();
		}

		return "redirect:/Users/Details/" + id;

	}

	@RequestMapping(method = RequestMethod.GET, value = "/serve/{id}/{width}/{height}")
	public void Serve(@PathVariable String id, @PathVariable int width,
			@PathVariable int height, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		BlobKey blobKey = new BlobKey(id);
		// blobstoreService.serve(blobKey, response);
		ImagesService imagesService = ImagesServiceFactory.getImagesService();

		Image oldImage = ImagesServiceFactory.makeImageFromBlob(blobKey);
		Transform resize = ImagesServiceFactory.makeResize(width, height);

		Image newImage = imagesService.applyTransform(resize, oldImage);

		byte[] newImageData = newImage.getImageData();
		response.getOutputStream().write(newImageData);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/serveVideo/{id}")
	public void ServeVideo(@PathVariable String id, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		BlobKey blobKey = new BlobKey(id);
		blobstoreService.serve(blobKey, response);
	}
}
