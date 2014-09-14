package models;

import javax.validation.*;
import play.data.validation.Constraints.*;

import java.util.*;
import models.*;

public class VotekeyGeneration {

    @Required
    public Long number;

    public VotekeyGeneration() {
    }

    public VotekeyGeneration(final Long number) {
        this.number = number;
    }

    public List<Votekey> generate() {
        List<Votekey> l = new ArrayList<Votekey>();

        if (number < 1) {
            return l;
        }
        for (long i = 0 ; i < number ; i++) {
            Votekey v = new Votekey(null, String.format("DJS%08d",i));
            l.add(v);
        }

        return l;
    }

    public String toString() {
        return "number of votekey to generate: " + number;
    }
}
