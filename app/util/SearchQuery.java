package util;


import models.*;
import models.durableArticles.Auction;
import models.durableArticles.AuctionDetail;
import models.durableArticles.Auction_D_Committee;
import models.durableArticles.Auction_E_Committee;
import models.durableArticles.Auction_FF_Committee;
import models.durableArticles.Borrow;
import models.durableArticles.BorrowDetail;
import models.durableArticles.Donation;
import models.durableArticles.DonationDetail;
import models.durableArticles.Donation_D_Committee;
import models.durableArticles.Donation_FF_Committee;
import models.durableArticles.DurableArticles;
import models.durableArticles.InternalTransfer;
import models.durableArticles.InternalTransferDetail;
import models.durableArticles.OtherTransfer;
import models.durableArticles.OtherTransferDetail;
import models.durableArticles.OtherTransfer_D_Committee;
import models.durableArticles.OtherTransfer_FF_Committee;
import models.durableArticles.Repairing;
import models.durableArticles.RepairingDetail;
import models.durableGoods.DurableGoods;
import models.type.ExportStatus;
import models.type.ImportStatus;
import models.consumable.Requisition;
import models.consumable.RequisitionDetail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SearchQuery {
	public static ArrayList<User> getUsers(String query){
		Set<User> set = User.find.where().ilike("username", "%"+query+"%").findSet();
		set.addAll(User.find.where().ilike("departure", "%"+query+"%").findSet());
		set.addAll(User.find.where().ilike("position", "%"+query+"%").findSet());
		set.addAll(User.find.where().ilike("namePrefix", "%"+query+"%").findSet());
		set.addAll(User.find.where().ilike("firstName", "%"+query+"%").findSet());
		set.addAll(User.find.where().ilike("lastName", "%"+query+"%").findSet());
		set.addAll(User.find.where().ilike("status.name", "%"+query+"%").findSet());

		return new ArrayList<User>(set);
	}
	
	public static ArrayList<Company> getCompanies(String query){
		Set<Company> set = Company.find.where().ilike("typeEntrepreneur", "%"+query+"%").findSet();
		set.addAll(Company.find.where().ilike("nameEntrepreneur", "%"+query+"%").findSet());
		set.addAll(Company.find.where().ilike("typedealer", "%"+query+"%").findSet());
		set.addAll(Company.find.where().ilike("nameDealer", "%"+query+"%").findSet());
		set.addAll(Company.find.where().ilike("payCodition", "%"+query+"%").findSet());
		set.addAll(Company.find.where().ilike("payPeriod", "%"+query+"%").findSet());
		set.addAll(Company.find.where().ilike("sendPeriod", "%"+query+"%").findSet());
		set.addAll(Company.find.where().ilike("durableArticlesType", "%"+query+"%").findSet());
		set.addAll(Company.find.where().ilike("durableGoodsType", "%"+query+"%").findSet());
		set.addAll(Company.find.where().ilike("consumableGoodsType", "%"+query+"%").findSet());
		set.addAll(Company.find.where().ilike("otherDetail", "%"+query+"%").findSet());
		return new ArrayList<Company>(set);
	}
	
	public static ArrayList<MaterialCode> getMaterialCodes(String query){
		Set<MaterialCode> set = MaterialCode.find.where().ilike("code", "%"+query+"%").findSet();
		set.addAll(MaterialCode.find.where().ilike("description", "%"+query+"%").findSet());
		set.addAll(MaterialCode.find.where().ilike("classifier", "%"+query+"%").findSet());
		set.addAll(MaterialCode.find.where().ilike("minNumberToAlert", "%"+query+"%").findSet());
		set.addAll(MaterialCode.find.where().ilike("otherDetail", "%"+query+"%").findSet());
		set.addAll(MaterialCode.find.where().ilike("materialType.typeName", "%"+query+"%").findSet());
		set.addAll(MaterialCode.find.where().ilike("materialType.acronym", "%"+query+"%").findSet());
		return new ArrayList<MaterialCode>(set);
	}
	
	public static ArrayList<models.durableArticles.Procurement> getDurableArticleProcurement(String query){
		Set<models.durableArticles.Procurement> set = models.durableArticles.Procurement.find.where().ilike("title", "%"+query+"%").findSet();
		set.addAll(models.durableArticles.Procurement.find.where().ilike("contractNo", "%"+query+"%").findSet());
		set.addAll(models.durableArticles.Procurement.find.where().ilike("budgetType", "%"+query+"%").findSet());
		set.addAll(models.durableArticles.Procurement.find.where().ilike("institute", "%"+query+"%").findSet());
		set.addAll(models.durableArticles.Procurement.find.where().ilike("barCode", "%"+query+"%").findSet());
		
		List<models.durableArticles.Procurement> ps = models.durableArticles.Procurement.find.where().eq("status", ImportStatus.SUCCESS).findList();
		ps.addAll(models.durableArticles.Procurement.find.where().eq("status", ImportStatus.UNCHANGE).findList());
		for(models.durableArticles.Procurement p : ps){
			boolean flag = true;
			if(dateValidator(p.addDate, query) || dateValidator(p.checkDate, query)){
				set.add(p);
				continue;
			}
			for(models.durableArticles.AI_Committee ai : p.aiCommittee){
				if(committeeValidator(ai, query)){
					set.add(p);
					flag = false;
					break;
				}
			}
			if(!flag) continue;
			for(models.durableArticles.EO_Committee eo : p.eoCommittee){
				if(committeeValidator(eo, query)){
					set.add(p);
					flag = false;
					break;
				}
			}
			if(!flag) continue;
			for(models.durableArticles.ProcurementDetail pd : p.details){
				if((pd.description!=null && pd.description.contains(query)) || 
						pd.phone!=null && pd.phone.contains(query) || 
						pd.brand!=null && pd.brand.contains(query) || 
						pd.seller!=null && pd.seller.contains(query) ||
						pd.serialNumber!=null && pd.serialNumber.contains(query)){
					set.add(p);
					break;
				}
				for(DurableArticles d : pd.subDetails){
					if(d.barCode!=null && d.barCode.contains("query") || 
							d.code!=null && d.code.equals(query) || 
							d.department!=null && d.department.contains(query) || 
							d.room!=null && d.room.contains(query)){
						set.add(p);
						flag = false;
						break;
					}
				}
				if(!flag) break;
			}
		}
		
		return new ArrayList<models.durableArticles.Procurement>(set);
	}
	
	public static ArrayList<models.durableGoods.Procurement> getDurableGoodsProcurement(String query){
		Set<models.durableGoods.Procurement> set = models.durableGoods.Procurement.find.where().ilike("title", "%"+query+"%").findSet();
		set.addAll(models.durableGoods.Procurement.find.where().ilike("contractNo", "%"+query+"%").findSet());
		set.addAll(models.durableGoods.Procurement.find.where().ilike("budgetType", "%"+query+"%").findSet());
		set.addAll(models.durableGoods.Procurement.find.where().ilike("institute", "%"+query+"%").findSet());
		set.addAll(models.durableGoods.Procurement.find.where().ilike("barCode", "%"+query+"%").findSet());
		
		List<models.durableGoods.Procurement> ps = models.durableGoods.Procurement.find.where().eq("status", ImportStatus.SUCCESS).findList();
		ps.addAll(models.durableGoods.Procurement.find.where().eq("status", ImportStatus.UNCHANGE).findList());
		for(models.durableGoods.Procurement p : ps){
			boolean flag = true;
			if(dateValidator(p.addDate, query) || dateValidator(p.checkDate, query)){
				set.add(p);
				continue;
			}
			for(models.durableGoods.AI_Committee ai : p.aiCommittee){
				if(committeeValidator(ai, query)){
					set.add(p);
					flag = false;
					break;
				}
			}
			if(!flag) continue;
			for(models.durableGoods.ProcurementDetail pd : p.details){
				if(pd.description!=null && pd.description.contains(query) || 
						pd.phone!=null && pd.phone.contains(query) || 
						pd.brand!=null && pd.brand.contains(query) || 
						pd.seller!=null && pd.seller.contains(query) ||
						pd.serialNumber!=null && pd.serialNumber.contains(query)){
					set.add(p);
					break;
				}
				for(DurableGoods d : pd.subDetails){
					if(d.barCode!=null && d.barCode.contains("query") || 
							d.codes!=null && d.codes.equals(query) || 
							d.department!=null && d.department.contains(query) || 
							d.room!=null && d.room.contains(query)){
						set.add(p);
						flag = false;
						break;
					}
				}
				if(!flag) break;
			}
		}
		
		return new ArrayList<models.durableGoods.Procurement>(set);
	}
	
	public static ArrayList<Requisition> getRequisition(String query){
		Set<Requisition> set = Requisition.find.where().ilike("title", "%"+query+"%").findSet();
		set.addAll(Requisition.find.where().ilike("number", "%"+query+"%").findSet());
		List<Requisition> rs = Requisition.find.where().eq("status", ExportStatus.SUCCESS).findList();
		for(Requisition r : rs){
			if(dateValidator(r.approveDate, query) || userValidator(r.approver, query) || userValidator(r.user, query)){
				set.add(r);
				continue;
			}
			for(RequisitionDetail rd : r.details){
				if(rd.code!=null){
					if(rd.description!=null && rd.description.contains(query) || 
							rd.code!=null && (rd.code.code!=null && rd.code.code.contains(query) || 
							rd.code.description!=null && rd.code.description.contains(query) )|| 
							userValidator(rd.withdrawer, query)){
						set.add(r);
						break;
					}
				}
			}
		}
		return new ArrayList<Requisition>(set); 
	}
	
	public static ArrayList<InternalTransfer> getInternalTransfer(String query){
		Set<InternalTransfer> set = InternalTransfer.find.where().ilike("title", query).findSet();
		set.addAll(InternalTransfer.find.where().ilike("number", query).findSet());
		List<InternalTransfer> internals = InternalTransfer.find.where().eq("status", ExportStatus.SUCCESS).findList();
		for(InternalTransfer in : internals){
			if(dateValidator(in.approveDate, query) || userValidator(in.approver, query)){
				set.add(in);
				continue;
			}
			for(InternalTransferDetail ind : in.detail){
				if(ind.code!=null && ind.code.contains(query) || 
						ind.department!=null && ind.department.contains(query) || 
						ind.recieverposition!=null && ind.recieverposition.contains(query) ||
						ind.recieverFirstName!=null && ind.recieverFirstName.contains(query) || 
						ind.recieverLastName!=null && ind.recieverLastName.contains(query) || 
						ind.durableArticles!=null && (ind.durableArticles.barCode!=null && ind.durableArticles.barCode.contains(query)||
						ind.durableArticles.code!=null && ind.durableArticles.code.contains(query) || 
						ind.durableArticles.detail!=null && ind.durableArticles.detail.fsn!=null && ind.durableArticles.detail.fsn.descriptionDescription.contains(query))){
					set.add(in);
					break;
				}
			}
		}
		
		return new ArrayList<InternalTransfer>(set);
	}

	public static ArrayList<Donation> getDonations(String query){
		Set<Donation> set = Donation.find.where().ilike("title", "%"+query+"%").findSet();
		set.addAll(Donation.find.where().ilike("contractNo", "%"+query+"%").findSet());
		set.addAll(Donation.find.where().ilike("company.nameEntrepreneur", "%"+query+"%").findSet());
		
		List<Donation> ds = Donation.find.where().eq("status", ExportStatus.SUCCESS).findList();
		for(Donation d : ds){
			boolean flag = true; 
			if(dateValidator(d.approveDate, query)){
				set.add(d);
				continue;
			}
			for(Donation_D_Committee dCommittee : d.dCommittee){
				if(committeeValidator(dCommittee, query)){
					set.add(d);
					flag = false;
					break;
				}
			}
			if(!flag) continue;
			for(Donation_FF_Committee dCommittee : d.ffCommittee){
				if(committeeValidator(dCommittee, query)){
					set.add(d);
					flag = false;
					break;
				}
			}
			if(!flag) continue;
			for(DonationDetail dd : d.detail){
				if(dd.annotation!=null && dd.annotation.contains(query) || 
						dd.code!=null && dd.code.contains(query) || 
						dd.durableArticles!=null && (dd.durableArticles.detail!=null && 
						dd.durableArticles.detail.fsn!=null && dd.durableArticles.detail.fsn.descriptionDescription.contains(query) || 
						dd.durableArticles.code!=null && dd.durableArticles.code.contains(query) || 
						dd.durableArticles.barCode!=null && dd.durableArticles.barCode.contains(query))){
					set.add(d);
					break;
				}
			}
		}
		return new ArrayList<Donation>(set);
	}
	
	public static ArrayList<Auction> getAuction(String query){
		Set<Auction> set = Auction.find.where().ilike("title", "%"+query+"%").findSet();
		set.addAll(Auction.find.where().ilike("contractNo", "%"+query+"%").findSet());
		set.addAll(Auction.find.where().ilike("company.nameEntrepreneur", "%"+query+"%").findSet());
		set.addAll(Auction.find.where().ilike("soldDestination", "%"+query+"%").findSet());
		set.addAll(Auction.find.where().ilike("telephoneNumber", "%"+query+"%").findSet());
		set.addAll(Auction.find.where().ilike("email", "%"+query+"%").findSet());
		
		List<Auction> as = Auction.find.where().eq("status",ExportStatus.SUCCESS).findList();
		for(Auction a : as){
			boolean flag = true; 
			if(dateValidator(a.approveDate, query)){
				set.add(a);
				continue;
			}
			for(Auction_D_Committee aCommittee : a.dCommittee){
				if(committeeValidator(aCommittee, query)){
					set.add(a);
					flag = false;
					break;
				}
			}
			if(!flag) continue;
			for(Auction_FF_Committee aCommittee : a.ffCommittee){
				if(committeeValidator(aCommittee, query)){
					set.add(a);
					flag = false;
					break;
				}
			}
			if(!flag) continue;
			for(Auction_E_Committee aCommittee : a.eCommittee){
				if(committeeValidator(aCommittee, query)){
					set.add(a);
					flag = false;
					break;
				}
			}
			if(!flag) continue;
			for(AuctionDetail ad : a.detail){
				if(ad.code!=null && ad.code.contains(query) || 
						ad.durableArticles!=null && (ad.durableArticles.barCode!=null && ad.durableArticles.barCode.contains(query) || 
						ad.durableArticles.code!=null && ad.durableArticles.code.contains(query) ||
						ad.durableArticles.detail!=null && ad.durableArticles.detail.fsn!=null && ad.durableArticles.detail.fsn.descriptionDescription.contains(query))){
					set.add(a);
					break;
				}
			}
		}
		return new ArrayList<Auction>(set);
	}
	
	public static ArrayList<OtherTransfer> getOtherTransfer(String query){
		Set<OtherTransfer> set = OtherTransfer.find.where().ilike("title", "%"+query+"%").findSet();
		set.addAll(OtherTransfer.find.where().ilike("number", "%"+query+"%").findSet());
		set.addAll(OtherTransfer.find.where().ilike("description", "%"+query+"%").findSet());
		
		List<OtherTransfer> os = OtherTransfer.find.where().eq("status",ExportStatus.SUCCESS).findList();
		for(OtherTransfer o : os){
			boolean flag = true; 
			if(dateValidator(o.approveDate, query)){
				set.add(o);
				continue;
			}
			for(OtherTransfer_D_Committee aCommittee : o.dCommittee){
				if(committeeValidator(aCommittee, query)){
					set.add(o);
					flag = false;
					break;
				}
			}
			if(!flag) continue;
			for(OtherTransfer_FF_Committee aCommittee : o.ffCommittee){
				if(committeeValidator(aCommittee, query)){
					set.add(o);
					flag = false;
					break;
				}
			}
			if(!flag) continue;
			for(OtherTransferDetail od : o.detail){
				if(od.descliption!=null && od.descliption.contains(query) || 
						od.durableArticles!=null && (od.durableArticles.barCode!=null && od.durableArticles.barCode.contains(query) || 
						od.durableArticles.code!=null && od.durableArticles.code.contains(query) ||
						od.durableArticles.detail!=null && od.durableArticles.detail.fsn!=null && od.durableArticles.detail.fsn.descriptionDescription.contains(query))){
					set.add(o);
					break;
				}
			}
		}
		return new ArrayList<OtherTransfer>(set);
	}
	
	public static ArrayList<Borrow> getBorrow(String query){
		Set<Borrow> set = Borrow.find.where().ilike("title", "%"+query+"%").findSet();
		set.addAll(Borrow.find.where().ilike("number", "%"+query+"%").findSet());
		
		List<Borrow> bs = Borrow.find.where().eq("status",ExportStatus.SUCCESS).findList();
		bs.addAll(Borrow.find.where().eq("status",ExportStatus.BORROW).findList());
		for(Borrow b : bs){
			if(dateValidator(b.dateOfEndBorrow, query) || dateValidator(b.dateOfStartBorrow, query) || 
					userValidator(b.approver, query) || userValidator(b.user, query)){
				set.add(b);
				continue;
			}
			for(BorrowDetail bd : b.detail){
				if(bd.description!=null && bd.description.contains(query) || 
						bd.durableArticles!=null && (bd.durableArticles.barCode!=null && bd.durableArticles.barCode.contains(query) || 
						bd.durableArticles.code!=null && bd.durableArticles.code.contains(query) ||
						bd.durableArticles.detail!=null && bd.durableArticles.detail.fsn!=null && bd.durableArticles.detail.fsn.descriptionDescription.contains(query))){
					set.add(b);
					break;
				}
			}
		}

		return new ArrayList<Borrow>(set);
	}
	
	public static ArrayList<Repairing> getRepair(String query){
		Set<Repairing> set = Repairing.find.where().ilike("title", "%"+query+"%").findSet();
		set.addAll(Repairing.find.where().ilike("number", "%"+query+"%").findSet());
		set.addAll(Repairing.find.where().ilike("company.nameEntrepreneur", "%"+query+"%").findSet());
		
		List<Repairing> rs = Repairing.find.where().eq("status",ExportStatus.SUCCESS).findList();
		rs.addAll(Repairing.find.where().eq("status",ExportStatus.REPAIRING).findList());
		for(Repairing r : rs){
			if(dateValidator(r.dateOfReceiveFromRepair, query) || dateValidator(r.dateOfSentToRepair, query) || userValidator(r.approver, query)){
				set.add(r);
				continue;
			}
			for(RepairingDetail rd : r.detail){
				if(rd.description!=null && rd.description.contains(query) || 
						rd.durableArticles!=null && (rd.durableArticles.barCode!=null && rd.durableArticles.barCode.contains(query) || 
						rd.durableArticles.code!=null && rd.durableArticles.code.contains(query) ||
						rd.durableArticles.detail!=null && rd.durableArticles.detail.fsn!=null && rd.durableArticles.detail.fsn.descriptionDescription.contains(query))){
					set.add(r);
					break;
				}
			}
		}

		return new ArrayList<Repairing>(set);
	}
	
	public static boolean dateValidator(Date date,String query){
		if(date == null) return false;
		String dt1 = new SimpleDateFormat("dd/MM/yyyy",new Locale("th","th")).format(date);
		String dt2 = new SimpleDateFormat("MMM",new Locale("th","th")).format(date);
		String dt3 = new SimpleDateFormat("MMMM",new Locale("th","th")).format(date);
		String dt4 = new SimpleDateFormat("MMM").format(date);
		String dt5 = new SimpleDateFormat("MMMM").format(date);
		String dt6 = new SimpleDateFormat("yyyy").format(date);
		
		if(dt1.contains(query) || dt2.contains(query) || dt3.contains(query) || 
		   dt4.contains(query) || dt5.contains(query) || dt6.contains(query)){
			return true;
		}
		
		return false;
	}
	
	public static boolean committeeValidator(models.durableArticles.AI_Committee d, String query){
		if(d==null) return false;
		if(d.committeePosition.contains(query) || d.employeesType.contains(query) || userValidator(d.committee, query)){
			return true;
		}
		return false;
	}
	
	public static boolean committeeValidator(models.durableArticles.EO_Committee d, String query){
		if(d==null) return false;
		if(d.committeePosition.contains(query) || d.employeesType.contains(query) || userValidator(d.committee, query)){
			return true;
		}
		return false;
	}
	
	public static boolean committeeValidator(models.durableGoods.AI_Committee d, String query){
		if(d==null) return false;
		if(d.committeePosition.contains(query) || d.employeesType.contains(query) || userValidator(d.committee, query)){
			return true;
		}
		return false;
	}
	
	public static boolean committeeValidator(Donation_D_Committee d, String query){
		if(d==null) return false;
		if(d.committeePosition.contains(query) || d.employeesType.contains(query) || userValidator(d.user, query)){
			return true;
		}
		return false;
	}
	
	public static boolean committeeValidator(Donation_FF_Committee d, String query){
		if(d==null) return false;
		if(d.committeePosition.contains(query) || d.employeesType.contains(query) || userValidator(d.user, query)){
			return true;
		}
		return false;
	}
	
	public static boolean committeeValidator(Auction_D_Committee d, String query){
		if(d==null) return false;
		if(d.committeePosition.contains(query) || d.employeesType.contains(query) || userValidator(d.user, query)){
			return true;
		}
		return false;
	}
	
	public static boolean committeeValidator(Auction_FF_Committee d, String query){
		if(d==null) return false;
		if(d.committeePosition.contains(query) || d.employeesType.contains(query) || userValidator(d.user, query)){
			return true;
		}
		return false;
	}
	
	public static boolean committeeValidator(Auction_E_Committee d, String query){
		if(d==null) return false;
		if(d.committeePosition.contains(query) || d.employeesType.contains(query) || userValidator(d.user, query)){
			return true;
		}
		return false;
	}
	
	public static boolean committeeValidator(OtherTransfer_D_Committee d, String query){
		if(d==null) return false;
		if(d.committeePosition.contains(query) || d.employeesType.contains(query) || userValidator(d.user, query)){
			return true;
		}
		return false;
	}
	
	public static boolean committeeValidator(OtherTransfer_FF_Committee d, String query){
		if(d==null) return false;
		if(d.committeePosition.contains(query) || d.employeesType.contains(query) || userValidator(d.user, query)){
			return true;
		}
		return false;
	}
	
	public static boolean userValidator(User u, String query){
		if(u == null) return false;
		if(u.departure.contains(query) || u.firstName.contains(query) || u.lastName.contains(query) ||
				u.namePrefix.contains(query) || u.position.contains(query) || u.username.contains(query)){
			return true;
		}
		return false;
	}
	
	/*public static boolean userValidator(Committee c, String query){
		if(c.title.contains(query) || c.firstName.contains(query) || c.lastName.contains(query) ||
				c.identificationNo.contains(query) || c.position.contains(query)){
			return true;
		}
		return false;
	}*/
}
