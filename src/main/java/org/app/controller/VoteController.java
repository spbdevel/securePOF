package org.app.controller;

import org.app.entity.Restaurant;
import org.app.entity.User;
import org.app.entity.Vote;
import org.app.repository.UserRepository;
import org.app.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

@RequestMapping("/rest")
@RestController()
public class VoteController extends AbstractController {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;


    @RequestMapping(value = "/votes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> todayVotes() {
        List<Date> timeFrame = getTimeFrame();
        return voteRepository.findToday(timeFrame.get(0), timeFrame.get(1));
    }


    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/votes/{restaurantId}", method = RequestMethod.GET)
    public synchronized boolean vote(@PathVariable("restaurantId")Long id) throws Exception {
        LocalDateTime currentTime = LocalDateTime.now();
        if(currentTime.getHour() >= 11)
            throw new Exception("It is too late to vote");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userRepository.findByAccountName(name);

        List<Date> timeFrame = getTimeFrame();
        Vote vote = voteRepository.findByUserToday(user, timeFrame.get(0), timeFrame.get(1));
        if(vote == null) {
            vote = new Vote();
            vote.setUser(user);
        }
        Restaurant rest = restauRepository.findById(id).orElseThrow(NoSuchElementException::new);;
        vote.setRestaurant(rest);
        vote.setModified(new Date());
        voteRepository.save(vote);
        return true;
    }

    private List<Date> getTimeFrame() {
        List<Date> res = new LinkedList<>();
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        Date start = instance.getTime();
        instance.set(Calendar.HOUR_OF_DAY, 24);
        Date end = instance.getTime();
        res.add(start);
        res.add(end);
        return res;
    }


}
