import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class User {
    private String username;
    private String password; 
    private boolean isAuctioneer;  

    public User(String username, String password, boolean isAuctioneer) {
        this.username = username;
        this.password = password;
        this.isAuctioneer = isAuctioneer;
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password); 
    }

    public boolean isAuctioneer() {
        return isAuctioneer;
    }
}

class Item {
    private String name;
    private double startingPrice;

    public Item(String name, double startingPrice) {
        this.name = name;
        this.startingPrice = startingPrice;
    }

    public String getName() {
        return name;
    }

    public double getStartingPrice() {
        return startingPrice;
    }
}

class Bid {
    private User bidder;
    private double amount;

    public Bid(User bidder, double amount) {
        this.bidder = bidder;
        this.amount = amount;
    }

    public User getBidder() {
        return bidder;
    }

    public double getAmount() {
        return amount;
    }
}

class Auction {
    private Item item;
    private List<Bid> bids;

    public Auction(Item item) {
        this.item = item;
        this.bids = new ArrayList<>();
    }

    public void placeBid(User bidder, double amount) {
        if (amount >= item.getStartingPrice()) {
            bids.add(new Bid(bidder, amount));
            System.out.println(bidder.getUsername() + " placed a bid of $" + amount + " on " + item.getName());
        } else {
            System.out.println("Bid must be at least the starting price of $" + item.getStartingPrice());
        }
    }

    public void determineWinner() {
        if (bids.isEmpty()) {
            System.out.println("No bids placed on " + item.getName());
            return;
        }

        Bid highestBid = bids.get(0);
        for (Bid bid : bids) {
            if (bid.getAmount() > highestBid.getAmount()) {
                highestBid = bid;
            }
        }

        System.out.println("The winner is " + highestBid.getBidder().getUsername() +
                " with a bid of $" + highestBid.getAmount());
    }

    public String getItemName() {
        return item.getName();
    }

    public double getItemStartingPrice() {
        return item.getStartingPrice();
    }
}

public class OnlineAuctionSystem {
    private List<User> users;
    private List<Auction> auctions;
    private Scanner scanner;

    public OnlineAuctionSystem() {
        this.users = new ArrayList<>();
        this.auctions = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public void registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Are you an auctioneer? (yes/no): ");
        boolean isAuctioneer = scanner.nextLine().equalsIgnoreCase("yes");

        users.add(new User(username, password, isAuctioneer));
        System.out.println("User registered successfully.");
    }

    public User loginUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.authenticate(password)) {
                System.out.println("Login successful.");
                return user;
            }
        }

        System.out.println("Invalid credentials.");
        return null;
    }

    public void createAuction(User auctioneer) {
        System.out.print("Enter item name: ");
        String itemName = scanner.nextLine();
        System.out.print("Enter starting price: ");
        double startingPrice = Double.parseDouble(scanner.nextLine());

        Item item = new Item(itemName, startingPrice);
        Auction auction = new Auction(item);
        auctions.add(auction);
        System.out.println("Auction created for " + itemName + " with starting price $" + startingPrice);
    }

    // Allow bidders to place bids
    public void placeBid(User bidder) {
        System.out.println("Available auctions:");
        for (int i = 0; i < auctions.size(); i++) {
            Auction auction = auctions.get(i);
            System.out.println((i + 1) + ". " + auction.getItemName() + " (Starting price: $" + auction.getItemStartingPrice() + ")");
        }

        System.out.print("Select auction to bid on: ");
        int auctionIndex = Integer.parseInt(scanner.nextLine()) - 1;

        if (auctionIndex >= 0 && auctionIndex < auctions.size()) {
            Auction selectedAuction = auctions.get(auctionIndex);
            System.out.print("Enter your bid amount: ");
            double bidAmount = Double.parseDouble(scanner.nextLine());

            selectedAuction.placeBid(bidder, bidAmount);
        } else {
            System.out.println("Invalid auction selection.");
        }
    }

    public void determineAuctionWinner() {
        System.out.println("Available auctions:");
        for (int i = 0; i < auctions.size(); i++) {
            Auction auction = auctions.get(i);
            System.out.println((i + 1) + ". " + auction.getItemName());
        }

        System.out.print("Select auction to determine the winner: ");
        int auctionIndex = Integer.parseInt(scanner.nextLine()) - 1;

        if (auctionIndex >= 0 && auctionIndex < auctions.size()) {
            Auction selectedAuction = auctions.get(auctionIndex);
            selectedAuction.determineWinner();
        } else {
            System.out.println("Invalid auction selection.");
        }
    }

    public void run() {
        System.out.println("Welcome to the Online Auction System");

        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                registerUser();
            } else if (choice == 2) {
                User loggedInUser = loginUser();
                if (loggedInUser != null) {
                    if (loggedInUser.isAuctioneer()) {
                        System.out.println("1. Create Auction");
                        System.out.println("2. Determine Auction Winner");
                        int auctioneerChoice = Integer.parseInt(scanner.nextLine());

                        if (auctioneerChoice == 1) {
                            createAuction(loggedInUser);
                        } else if (auctioneerChoice == 2) {
                            determineAuctionWinner();
                        } else {
                            System.out.println("Invalid choice.");
                        }
                    } else {
                        System.out.println("1. Place Bid");
                        int bidderChoice = Integer.parseInt(scanner.nextLine());

                        if (bidderChoice == 1) {
                            placeBid(loggedInUser);
                        } else {
                            System.out.println("Invalid choice.");
                        }
                    }
                }
            } else if (choice == 3) {
                System.out.println("Exiting the system. Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice, please try again.");
            }
        }
    }

    public static void main(String[] args) {
        OnlineAuctionSystem system = new OnlineAuctionSystem();
        system.run();
    }
}
