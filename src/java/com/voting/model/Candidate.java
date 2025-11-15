package com.voting.model;

public class Candidate {
    private int candidateId;
    private String name;
    private String party;
    private String symbolUrl; // Path to the candidate's symbol image
    private int voteCount;

    // Default Constructor
    public Candidate() {
    }

    // Constructor for adding a new candidate
    public Candidate(String name, String party, String symbolUrl) {
        this.name = name;
        this.party = party;
        this.symbolUrl = symbolUrl;
        this.voteCount = 0; // Starts at 0
    }

    // --- Getters and Setters ---

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getSymbolUrl() {
        return symbolUrl;
    }

    public void setSymbolUrl(String symbolUrl) {
        this.symbolUrl = symbolUrl;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}