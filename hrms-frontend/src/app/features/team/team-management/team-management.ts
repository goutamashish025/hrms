import { Component, OnInit } from '@angular/core';
import { TeamService } from '../../../core/services/team';

@Component({
  selector: 'app-team-management',
  templateUrl: './team-management.html',
  styleUrls: ['./team-management.scss']
})
export class TeamManagementComponent implements OnInit {

  teams: any[] = [];
  members: any[] = [];

  selectedTeamId!: number;

  constructor(private teamService: TeamService) {}

  ngOnInit(): void {
    this.loadTeams();
  }

  loadTeams(): void {
    this.teamService.getTeams().subscribe({
      next: (res) => {
        this.teams = res;
      }
    });
  }

  selectTeam(team: any): void {

    this.selectedTeamId = team.id;

    this.teamService.getTeamMembers(team.id)
      .subscribe({
        next: (res) => {
          this.members = res;
        }
      });
  }
}