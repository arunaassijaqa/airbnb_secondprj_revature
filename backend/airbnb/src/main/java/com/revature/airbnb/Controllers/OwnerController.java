package com.revature.airbnb.Controllers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.revature.airbnb.Exceptions.InvalidAuthenticationException;
import com.revature.airbnb.Exceptions.InvalidRegistrationException;
import com.revature.airbnb.Exceptions.UserNotFoundException;
import com.revature.airbnb.Exceptions.UsernameAlreadyTakenException;
import com.revature.airbnb.Models.Listing;
import com.revature.airbnb.Models.Owner;
import com.revature.airbnb.Models.Booking;
import com.revature.airbnb.Services.BookingService;
import com.revature.airbnb.Services.ListingService;
import com.revature.airbnb.Services.OwnerService;

import jakarta.servlet.http.HttpSession;

import static org.springframework.http.HttpStatus.*;
import org.springframework.http.HttpStatus;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/owners")
public class OwnerController {

    private final OwnerService ownerService;
    private final ListingService listingService;
    private final BookingService bookingService;

    @Autowired
    public OwnerController(OwnerService ownerService, ListingService listingService, BookingService bookingService) {
        this.ownerService = ownerService;
        this.listingService = listingService;
        this.bookingService = bookingService;
    }

    /*This function registers an Owner by adding their username, password, and email to the Owners table */
    @PostMapping("/register")
    public ResponseEntity<Owner> registerOwner(@RequestBody Owner owner, HttpSession session) {
        Owner savedOwner;

        try {
            savedOwner = ownerService.registerOwner(owner.getUsername(), owner.getPassword(), owner.getEmail());
            session.setAttribute("user", savedOwner);
        } catch (UsernameAlreadyTakenException e) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        return new ResponseEntity<>(savedOwner, CREATED);
    }

    /*This function logs in an Owner by adding their token to the Owners table */
    @PostMapping("/login")
    public ResponseEntity<Owner> loginHandler(@RequestBody Owner owner, HttpSession session)
    {
        Owner loggedInOwner;
        try
        {
            loggedInOwner = ownerService.login(owner.getUsername(), owner.getPassword());
            session.setAttribute("user", loggedInOwner);
        }
        catch (InvalidAuthenticationException | UserNotFoundException e)
        {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        return new ResponseEntity<>(loggedInOwner, OK);
    }

    /*This function logs out an Owner by removing their token from the Owners table */
    @PostMapping("/logout")
    public ResponseEntity<Owner> logoutHandler(HttpSession session)
    {
        session.removeAttribute("user");
        session.invalidate();
        return new ResponseEntity<>(OK);
    }

    /*This function retrieves all Owners from the Owners table */
    @GetMapping
    public List<Owner> getAllOwners() {
        return ownerService.getAllOwners();
    }

    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> viewAccountDetails(@PathVariable int id) {
        try {
            Owner owner = ownerService.getOwnerById(id);
            Map<String, Object> accountDetails = new LinkedHashMap<>();
            accountDetails.put("username", owner.getUsername());
            accountDetails.put("email", owner.getEmail());
            accountDetails.put("listings", owner.getListings());
            return new ResponseEntity<>(accountDetails, OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    /* This function retrieves all listings for a particular Owner, using the Owner's ID */
    @GetMapping("{id}/bookings")
    public List<Booking> getAllBookings(@PathVariable int id) {
        return bookingService.getBookingsByOwnerId(id);
    }

    /* This function adds an entry in the Listings table, using the Owner's ID to determine its creator */
    @PostMapping("/{id}/listings")
    public Listing createListing(@RequestBody Listing listing, @PathVariable int id)  {
        Owner owner = ownerService.getOwnerById(id);
        return listingService.createListing(listing);
    }
    
    /* This function retrieves all listings for a particular Owner, using the Owner's ID */
    @GetMapping("{id}/listings")
    public List<Listing> getAllOwners(@PathVariable int id) {
        return ownerService.getOwnerListings(id);
    }

    @ExceptionHandler(InvalidRegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleInvalidRegistration(InvalidRegistrationException e)
    {
        return e.getMessage();
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleUsernameAlreadyTaken(UsernameAlreadyTakenException e)
    {
        return e.getMessage();
    }

    @ExceptionHandler(InvalidAuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String InvalidAuthenticationHandler(InvalidAuthenticationException e)
    {
        return e.getMessage();
    }
}
